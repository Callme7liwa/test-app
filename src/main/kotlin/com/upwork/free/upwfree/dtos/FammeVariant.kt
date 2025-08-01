package com.upwork.free.upwfree.dtos;

import com.fasterxml.jackson.annotation.JsonProperty


data class FammeVariant(
    val id: Long,
    val title: String,
    val price: String,
    @JsonProperty("inventory_quantity")
    val inventoryQuantity: Int?
)

