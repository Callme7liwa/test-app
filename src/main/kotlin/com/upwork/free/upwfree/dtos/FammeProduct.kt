package com.upwork.free.upwfree.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class FammeProduct(
    val id: Long,
    val title: String,
    val handle: String,
    @JsonProperty("product_type")
    val productType: String?,
    val vendor: String?,
    val tags: String?,
    val variants: List<FammeVariant>
)