package com.upwork.free.upwfree.models

import com.fasterxml.jackson.annotation.JsonProperty

data class FammeProduct(
    val id: Long,
    val title: String,
    val handle: String,
    @JsonProperty("product_type")
    val productType: String?,
    val vendor: String?,
    val tags: List<String>?, // Tags sont un array dans l'API
    val variants: List<FammeVariant>
)