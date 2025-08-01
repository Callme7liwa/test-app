package com.upwork.free.upwfree.entities

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime

data class Product (
    val id: Long? = null,
    val title: String,
    val price: BigDecimal,
    val handle: String,
    @JsonProperty("product_type")
    val productType: String? = null,
    val vendor: String? = null,
    val tags: String? = null,
    val variants: String? = null, // JSONB stored as String
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)