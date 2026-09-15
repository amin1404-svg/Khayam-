package com.example.data.model

data class UserProfile(
    val id: String = "user_001",
    val fullName: String = "کاربر گرامی عسل ساوالان",
    val phoneNumber: String = "۰۹۱۲۳۴۵۶۷۸۹",
    val email: String = "customer@savalanhoney.ir",
    val isVerified: Boolean = true,
    val isAdmin: Boolean = false,
    val loyaltyPoints: Int = 140,
    val joinedDate: String = "۱۴۰۳/۱۱/۱۰"
)

data class UserAddress(
    val id: Long = 1,
    val title: String = "منزل",
    val province: String = "تهران",
    val city: String = "تهران",
    val fullAddress: String = "خیابان سبلان شمالی، کوچه بهار، پلاک ۱۲، واحد ۴",
    val postalCode: String = "۱۶۴۵۸۹۳۲۱۱",
    val isDefault: Boolean = true
)
