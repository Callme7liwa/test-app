package com.upwork.free.upwfree.services

import com.upwork.free.upwfree.models.FammeProduct
import com.upwork.free.upwfree.models.Product


import com.fasterxml.jackson.databind.ObjectMapper
import com.upwork.free.upwfree.models.FammeProductResponse
import com.upwork.free.upwfree.repositories.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigDecimal

@Service
@Component
class ProductService(
    private val productRepository: ProductRepository,
    private val webClient: WebClient,
    private val objectMapper: ObjectMapper,
    @Value("\${app.products.fetch.url}") private val productsUrl: String,
    @Value("\${app.products.fetch.limit}") private val productLimit: Int
) {

    private val logger = LoggerFactory.getLogger(ProductService::class.java)

    fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    fun getProductById(id: Long): Product? {
        return productRepository.findById(id)
    }

    fun saveProduct(product: Product): Product {
        return productRepository.save(product)
    }

    fun deleteProduct(id: Long): Boolean {
        return productRepository.deleteById(id)
    }

    fun searchProducts(searchTerm: String): List<Product> {
        return productRepository.findByTitleContaining(searchTerm)
    }

    // Scheduled job with initialDelay=0 as required - DISABLED for testing
    @Scheduled(fixedRate = 3600000, initialDelay = 0) // Run every hour, start immediately
    fun fetchProductsFromFamme() {
        try {
            logger.info("Fetching products from Famme API: $productsUrl")

            val response = webClient
                .get()
                .uri(productsUrl)
                .retrieve()
                .bodyToMono(FammeProductResponse::class.java)
                .block()

            response?.products?.take(productLimit)?.forEach { fammeProduct ->
                try {
                    val product = convertFammeProductToProduct(fammeProduct)
                    // Check if product already exists by handle
                    val existingProduct = productRepository.findAll()
                        .find { it.handle == product.handle }

                    if (existingProduct == null) {
                        productRepository.save(product)
                        logger.debug("Saved new product: ${product.title}")
                    } else {
                        logger.debug("Product already exists: ${product.title}")
                    }
                } catch (e: Exception) {
                    logger.error("Error processing product: ${fammeProduct.title}", e)
                }
            }

            logger.info("Finished fetching products from Famme API")
        } catch (e: Exception) {
            logger.error("Error fetching products from Famme API", e)
        }
    }

    private fun convertFammeProductToProduct(fammeProduct: FammeProduct): Product {
        // Get price from first variant or default to 0
        val price = fammeProduct.variants.firstOrNull()?.price?.toBigDecimalOrNull() ?: BigDecimal.ZERO

        // Convert variants to JSON string
        val variantsJson = objectMapper.writeValueAsString(fammeProduct.variants)

        return Product(
            title = fammeProduct.title,
            price = price,
            handle = fammeProduct.handle,
            productType = fammeProduct.productType,
            vendor = fammeProduct.vendor,
            tags = fammeProduct.tags, // ← Direct assignment, no conversion needed!
            variants = variantsJson
        )
    }
}