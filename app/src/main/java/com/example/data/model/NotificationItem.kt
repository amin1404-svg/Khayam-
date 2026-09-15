package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val dateText: String,
    val type: String = "general", // "discount", "order", "product", "brand"
    val isRead: Boolean = false
)
