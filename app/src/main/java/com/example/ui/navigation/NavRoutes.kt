package com.example.ui.navigation

sealed class NavScreen(val route: String) {
    object Splash : NavScreen("splash")
    object Home : NavScreen("home")
    object Shop : NavScreen("shop")
    object Cart : NavScreen("cart")
    object Checkout : NavScreen("checkout")
    object Orders : NavScreen("orders")
    object Profile : NavScreen("profile")
    object Auth : NavScreen("auth")
    object ContactProducer : NavScreen("contact_producer")
    object Favorites : NavScreen("favorites")
    object Notifications : NavScreen("notifications")
    object AdminDashboard : NavScreen("admin_dashboard")

    object ProductDetail : NavScreen("product_detail/{productId}") {
        fun createRoute(productId: Long) = "product_detail/$productId"
    }

    object OrderTracking : NavScreen("order_tracking/{orderId}") {
        fun createRoute(orderId: Long) = "order_tracking/$orderId"
    }

    object OrderSuccess : NavScreen("order_success/{orderId}") {
        fun createRoute(orderId: Long) = "order_success/$orderId"
    }
}
