package com.upwork.free.upwfree.controllers

import com.upwork.free.upwfree.models.Product
import com.upwork.free.upwfree.services.ProductService


import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@Controller
@RequestMapping(path = [""])
class ProductController(private val productService: ProductService) {

    @GetMapping("")
    fun index(model: Model): String {
        model.addAttribute("pageTitle", "From Othman")
        return "index"
    }

    // Load products via HTMX
    @GetMapping("products")
    fun loadProducts(model: Model): String {
        val products = productService.getAllProducts()
        model.addAttribute("products", products)
        return "fragments/product-table"
    }

    // Add new product via HTMX form
    @PostMapping("products")
    fun addProduct(
        @RequestParam title: String,
        @RequestParam price: String,
        @RequestParam handle: String,
        @RequestParam(required = false) vendor: String?,
        @RequestParam(required = false) productType: String?,
        model: Model
    ): String {
        try {
            val product = Product(
                title = title.trim(),
                price = BigDecimal(price),
                handle = handle.trim(),
                vendor = vendor?.trim(),
                productType = productType?.trim()
            )

            productService.saveProduct(product)

            // Return updated product table
            val products = productService.getAllProducts()
            model.addAttribute("products", products)
            model.addAttribute("success", "Product added successfully!")

        } catch (e: Exception) {
            val products = productService.getAllProducts()
            model.addAttribute("products", products)
            model.addAttribute("error", "Error adding product: ${e.message}")
        }

        return "fragments/product-table"
    }

    // Delete product via HTMX
    @DeleteMapping("products/{id}")
    fun deleteProduct(@PathVariable id: Long, model: Model): String {
        try {
            val deleted = productService.deleteProduct(id)
            if (deleted) {
                model.addAttribute("success", "Product deleted successfully!")
            } else {
                model.addAttribute("error", "Product not found!")
            }
        } catch (e: Exception) {
            model.addAttribute("error", "Error deleting product: ${e.message}")
        }

        // Return updated product table
        val products = productService.getAllProducts()
        model.addAttribute("products", products)
        return "fragments/product-table"
    }

    // Update product page
    @GetMapping("products/{id}/edit")
    fun editProduct(@PathVariable id: Long, model: Model): String {
        val product = productService.getProductById(id)
        if (product == null) {
            return "redirect:/?error=Product not found"
        }

        model.addAttribute("product", product)
        model.addAttribute("pageTitle", "Edit Product")
        return "edit-product"
    }

    // Update product via HTMX
    @PutMapping("products/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestParam title: String,
        @RequestParam price: String,
        @RequestParam handle: String,
        @RequestParam(required = false) vendor: String?,
        @RequestParam(required = false) productType: String?,
        model: Model
    ): String {
        try {
            val existingProduct = productService.getProductById(id)
            if (existingProduct == null) {
                model.addAttribute("error", "Product not found!")
                return "fragments/edit-form"
            }

            val updatedProduct = existingProduct.copy(
                title = title.trim(),
                price = BigDecimal(price),
                handle = handle.trim(),
                vendor = vendor?.trim(),
                productType = productType?.trim()
            )

            productService.saveProduct(updatedProduct)
            model.addAttribute("product", updatedProduct)
            model.addAttribute("success", "Product updated successfully!")

        } catch (e: Exception) {
            val product = productService.getProductById(id)
            model.addAttribute("product", product)
            model.addAttribute("error", "Error updating product: ${e.message}")
        }

        return "fragments/edit-form"
    }

    // Search page
    @GetMapping("search")
    fun searchPage(model: Model): String {
        model.addAttribute("pageTitle", "Search Products")
        return "search"
    }

    // Live search via HTMX
    @GetMapping("search/results")
    fun searchProducts(@RequestParam(required = false, defaultValue = "") q: String, model: Model): String {
        val products = if (q.isBlank()) {
            emptyList()
        } else {
            productService.searchProducts(q)
        }

        model.addAttribute("products", products)
        model.addAttribute("searchTerm", q)
        return "fragments/search-results"
    }
}