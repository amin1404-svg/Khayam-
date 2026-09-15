package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_messages")
data class ContactMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val senderName: String,
    val phoneNumber: String,
    val subject: String,
    val messageText: String,
    val dateText: String,
    val isReplied: Boolean = false
)
