package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String, // e.g. "SAV-10024"
    val orderDate: String, // e.g. "۱۴۰۴/۰۶/۲۵"
    val customerName: String,
    val customerPhone: String,
    val province: String,
    val city: String,
    val address: String,
    val postalCode: String,
    val orderNotes: String = "",
    val itemsSummary: String, // Comma-separated or readable summary
    val totalItemsCount: Int,
    val itemsSubtotalTomans: Long,
    val shippingCostTomans: Long,
    val discountTomans: Long = 0,
    val totalPayableTomans: Long,
    val shippingMethod: String, // "پست پیشتاز", "ارسال پیک اکسپرس", "ارسال توسط تولیدکننده"
    val paymentMethod: String, // "پرداخت آنلاین (شتابی)", "پرداخت در محل"
    val status: OrderStatus = OrderStatus.SUBMITTED,
    val trackingCode: String = "",
    val paymentTransactionId: String = ""
)
