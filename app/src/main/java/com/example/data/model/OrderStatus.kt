package com.example.data.model

enum class OrderStatus(val titleFa: String, val stepIndex: Int) {
    SUBMITTED("ثبت سفارش", 0),
    PREPARING("در حال آماده‌سازی", 1),
    HANDED_OVER("تحویل به شرکت ارسال", 2),
    SHIPPED("ارسال شده", 3),
    DELIVERED("تحویل داده شده", 4),
    CANCELLED("لغو شده", -1)
}
