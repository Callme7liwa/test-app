package com.upwork.free.upwfree.models;

import com.fasterxml.jackson.annotation.JsonProperty

data class FammeVariant(
    val id: Long,
    val title: String,
    val price: String, // Price comme string dans l'API
    val sku: String?,
    val available: Boolean,
    @JsonProperty("option1")
    val option1: String?,
    @JsonProperty("option2")
    val option2: String?,
    @JsonProperty("option3")
    val option3: String?
)

