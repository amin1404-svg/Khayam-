package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: Long,
    val productTitle: String,
    val selectedWeightGrams: Int,
    val unitPriceTomans: Long,
    val quantity: Int = 1,
    val imageDrawableName: String = "img_honey_natural"
) {
    val totalPriceTomans: Long
        get() = unitPriceTomans * quantity
}
