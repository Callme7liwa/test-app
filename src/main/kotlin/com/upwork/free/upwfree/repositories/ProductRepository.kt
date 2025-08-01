package com.upwork.free.upwfree.repositories

import com.upwork.free.upwfree.entities.Product


import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Component
import java.sql.ResultSet


@Repository
@Component
class ProductRepository(
    private val jdbcClient: JdbcClient,
    private val objectMapper: ObjectMapper
) {

    fun findAll(): List<Product> {
        return jdbcClient
            .sql("SELECT * FROM products ORDER BY created_at DESC")
            .query { rs, _ -> mapRowToProduct(rs) }
            .list()
    }

    fun findById(id: Long): Product? {
        return jdbcClient
            .sql("SELECT * FROM products WHERE id = ?")
            .param(id)
            .query { rs, _ -> mapRowToProduct(rs) }
            .optional()
            .orElse(null)
    }

    fun save(product: Product): Product {
        if (product.id == null) {
            // Insert new product
            val generatedId = jdbcClient
                .sql("""
                    INSERT INTO products (title, price, handle, product_type, vendor, tags, variants) 
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """)
                .param(product.title)
                .param(product.price)
                .param(product.handle)
                .param(product.productType)
                .param(product.vendor)
                .param(product.tags)
                .param(product.variants)
                .update()

            return product.copy(id = generatedId.toLong())
        } else {
            // Update existing product
            jdbcClient
                .sql("""
                    UPDATE products 
                    SET title = ?, price = ?, handle = ?, 
                        product_type = ?, vendor = ?, tags = ?,
                        variants = ?, updated_at = CURRENT_TIMESTAMP
                    WHERE id = ?
                """)
                .param(product.title)
                .param(product.price)
                .param(product.handle)
                .param(product.productType)
                .param(product.vendor)
                .param(product.tags)
                .param(product.variants)
                .param(product.id)
                .update()

            return product
        }
    }

    fun deleteById(id: Long): Boolean {
        val rowsAffected = jdbcClient
            .sql("DELETE FROM products WHERE id = ?")
            .param(id)
            .update()
        return rowsAffected > 0
    }

    fun findByTitleContaining(searchTerm: String): List<Product> {
        return jdbcClient
            .sql("SELECT * FROM products WHERE title ILIKE ? ORDER BY title")
            .param("%$searchTerm%")
            .query { rs, _ -> mapRowToProduct(rs) }
            .list()
    }

    private fun mapRowToProduct(rs: ResultSet): Product {
        return Product(
            id = rs.getLong("id"),
            title = rs.getString("title"),
            price = rs.getBigDecimal("price"),
            handle = rs.getString("handle"),
            productType = rs.getString("product_type"),
            vendor = rs.getString("vendor"),
            tags = rs.getString("tags"),
            variants = rs.getString("variants"),
            createdAt = rs.getTimestamp("created_at")?.toLocalDateTime(),
            updatedAt = rs.getTimestamp("updated_at")?.toLocalDateTime()
        )
    }
}