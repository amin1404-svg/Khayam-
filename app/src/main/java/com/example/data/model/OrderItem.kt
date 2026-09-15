package com.example.data.model

data class OrderItem(
    val productId: Long,
    val productTitle: String,
    val weightGrams: Int,
    val quantity: Int,
    val unitPriceTomans: Long,
    val totalPriceTomans: Long,
    val imageDrawableName: String = "img_honey_natural"
)
