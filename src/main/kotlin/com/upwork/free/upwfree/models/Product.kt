package com.upwork.free.upwfree.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime

data class Product(
    val id: Long? = null,
    val title: String,
    val price: BigDecimal,
    val handle: String,
    @JsonProperty("product_type")
    val productType: String? = null,
    val vendor: String? = null,
    val tags: List<String>? = null, // ← Changed to List<String>
    val variants: String? = null, // JSON stored as String
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)