package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subtitle: String,
    val description: String,
    val honeyType: String, // عسل طبیعی، عسل گون، عسل آویشن، عسل چندگیاه، محصولات ویژه
    val availableWeightsCsv: String = "250,500,1000,2000", // in grams
    val defaultWeightGrams: Int = 500,
    val basePriceTomans: Long, // Price for 500g
    val discountPercent: Int = 0, // 0 for no discount
    val stockQuantity: Int = 50,
    val isActive: Boolean = true,
    val isBestSeller: Boolean = false,
    val isSpecialOffer: Boolean = false,
    val imageDrawableName: String = "img_honey_natural",
    val harvestRegion: String = "دامنه‌های کوه سبلان (ساوالان)",
    val sucrosePercentage: Double = 1.6,
    val purityPercentage: Int = 100,
    val healthBenefits: String = "تقویت سیستم ایمنی، بهبود عملکرد دستگاه گوارش، انرژی‌بخش طبیعی، التیام‌بخش گلو و ضدالتهاب ارگانیک.",
    val isFavorite: Boolean = false
) {
    fun getAvailableWeights(): List<Int> {
        return availableWeightsCsv.split(",")
            .mapNotNull { it.trim().toIntOrNull() }
            .ifEmpty { listOf(250, 500, 1000, 2000) }
    }

    fun getPriceForWeight(weightGrams: Int): Long {
        // Linear price scaling with a small bulk discount for 1kg and 2kg
        val ratio = weightGrams.toDouble() / 500.0
        val multiplier = when {
            weightGrams >= 2000 -> 0.90 // 10% bulk discount
            weightGrams >= 1000 -> 0.95 // 5% bulk discount
            else -> 1.0
        }
        val calculated = (basePriceTomans * ratio * multiplier).toLong()
        // Round to nearest 1,000 Tomans
        return (calculated / 1000) * 1000
    }

    fun getDiscountedPrice(weightGrams: Int): Long {
        val original = getPriceForWeight(weightGrams)
        if (discountPercent <= 0) return original
        val discounted = original * (100 - discountPercent) / 100
        return (discounted / 1000) * 1000
    }
}
