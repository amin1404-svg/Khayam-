package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.CartItem
import com.example.data.model.ContactMessage
import com.example.data.model.NotificationItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.Product
import com.example.data.model.UserProfile
import com.example.data.repository.HoneyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HoneyViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    val repository = HoneyRepository(database)

    // User profile
    val currentUser: StateFlow<UserProfile> = repository.currentUser

    // All active products
    val allProducts: StateFlow<List<Product>> = repository.allActiveProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin products (including disabled)
    val adminProducts: StateFlow<List<Product>> = repository.allAdminProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Favorites
    val favoriteProducts: StateFlow<List<Product>> = repository.favoriteProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCartCount: StateFlow<Int> = repository.cartItems
        .combine(MutableStateFlow(0)) { items, _ -> items.sumOf { it.quantity } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Orders
    val orders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Notifications
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = repository.unreadNotificationsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Contact messages
    val contactMessages: StateFlow<List<ContactMessage>> = repository.contactMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Shop filtering & sorting state
    private val _selectedCategory = MutableStateFlow("همه")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortBy = MutableStateFlow("محبوب‌ترین")
    val sortBy: StateFlow<String> = _sortBy.asStateFlow()

    private val _selectedWeightFilter = MutableStateFlow<Int?>(null)
    val selectedWeightFilter: StateFlow<Int?> = _selectedWeightFilter.asStateFlow()

    // Coupon code state
    private val _appliedCouponCode = MutableStateFlow<String?>(null)
    val appliedCouponCode: StateFlow<String?> = _appliedCouponCode.asStateFlow()

    private val _couponDiscountAmount = MutableStateFlow<Long>(0L)
    val couponDiscountAmount: StateFlow<Long> = _couponDiscountAmount.asStateFlow()

    // Mock OTP and Login
    val lastGeneratedOtp = repository.lastGeneratedOtp
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    // Mock Payment Gateway State
    private val _isPaymentSheetOpen = MutableStateFlow(false)
    val isPaymentSheetOpen: StateFlow<Boolean> = _isPaymentSheetOpen.asStateFlow()

    private val _pendingOrderData = MutableStateFlow<PendingCheckoutInfo?>(null)
    val pendingOrderData: StateFlow<PendingCheckoutInfo?> = _pendingOrderData.asStateFlow()

    private val _lastPlacedOrder = MutableStateFlow<Order?>(null)
    val lastPlacedOrder: StateFlow<Order?> = _lastPlacedOrder.asStateFlow()

    // Filtered products flow
    val filteredProducts = combine(
        allProducts,
        _selectedCategory,
        _searchQuery,
        _sortBy,
        _selectedWeightFilter
    ) { products, category, query, sort, weightFilter ->
        var list = products

        // Category filter
        if (category != "همه") {
            list = list.filter { it.honeyType == category }
        }

        // Search filter
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.subtitle.lowercase().contains(q) ||
                it.description.lowercase().contains(q) ||
                it.honeyType.lowercase().contains(q)
            }
        }

        // Weight filter
        if (weightFilter != null) {
            list = list.filter { it.getAvailableWeights().contains(weightFilter) }
        }

        // Sorting
        when (sort) {
            "محبوب‌ترین" -> list.sortedByDescending { it.isBestSeller }
            "جدیدترین" -> list.sortedByDescending { it.id }
            "ارزان‌ترین" -> list.sortedBy { it.basePriceTomans }
            "گران‌ترین" -> list.sortedByDescending { it.basePriceTomans }
            else -> list
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.initializeSeedDataIfNeeded()
        }
    }

    // Filter setters
    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortBy(sort: String) {
        _sortBy.value = sort
    }

    fun setWeightFilter(weight: Int?) {
        _selectedWeightFilter.value = if (_selectedWeightFilter.value == weight) null else weight
    }

    // Cart actions
    fun addToCart(product: Product, weightGrams: Int = product.defaultWeightGrams, quantity: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(product, weightGrams, quantity)
        }
    }

    fun updateCartQuantity(item: CartItem, newQty: Int) {
        viewModelScope.launch {
            repository.updateCartItemQuantity(item, newQty)
        }
    }

    fun removeCartItem(item: CartItem) {
        viewModelScope.launch {
            repository.removeCartItem(item)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            repository.toggleFavorite(product)
        }
    }

    // Coupon verification
    fun applyCoupon(code: String): Boolean {
        val trimmed = code.trim().uppercase()
        return if (trimmed == "SAVALAN" || trimmed == "BAHAR" || trimmed == "TABIAT") {
            _appliedCouponCode.value = trimmed
            _couponDiscountAmount.value = 50000L
            true
        } else {
            false
        }
    }

    fun removeCoupon() {
        _appliedCouponCode.value = null
        _couponDiscountAmount.value = 0L
    }

    // Checkout & Payment
    fun initiateCheckout(
        customerName: String,
        customerPhone: String,
        province: String,
        city: String,
        address: String,
        postalCode: String,
        orderNotes: String,
        shippingMethod: String,
        paymentMethod: String,
        shippingCost: Long
    ) {
        val currentCart = cartItems.value
        if (currentCart.isEmpty()) return

        val info = PendingCheckoutInfo(
            customerName = customerName,
            customerPhone = customerPhone,
            province = province,
            city = city,
            address = address,
            postalCode = postalCode,
            orderNotes = orderNotes,
            shippingMethod = shippingMethod,
            paymentMethod = paymentMethod,
            items = currentCart,
            shippingCost = shippingCost,
            discount = couponDiscountAmount.value
        )
        _pendingOrderData.value = info

        if (paymentMethod.contains("آنلاین")) {
            _isPaymentSheetOpen.value = true
        } else {
            // Cash on delivery / direct submission
            finalizeOrder(info)
        }
    }

    fun onPaymentCompleted(success: Boolean) {
        _isPaymentSheetOpen.value = false
        val info = _pendingOrderData.value
        if (success && info != null) {
            finalizeOrder(info)
        }
    }

    private fun finalizeOrder(info: PendingCheckoutInfo) {
        viewModelScope.launch {
            val order = repository.createOrder(
                customerName = info.customerName,
                customerPhone = info.customerPhone,
                province = info.province,
                city = info.city,
                address = info.address,
                postalCode = info.postalCode,
                orderNotes = info.orderNotes,
                shippingMethod = info.shippingMethod,
                paymentMethod = info.paymentMethod,
                items = info.items,
                shippingCostTomans = info.shippingCost,
                discountTomans = info.discount
            )
            _lastPlacedOrder.value = order
            _pendingOrderData.value = null
            removeCoupon()
        }
    }

    // Contact producer
    fun sendContactMessage(name: String, phone: String, subject: String, message: String, onSent: () -> Unit) {
        viewModelScope.launch {
            repository.sendContactMessage(name, phone, subject, message)
            onSent()
        }
    }

    // Notifications
    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    // Auth & OTP
    fun sendOtp(phoneNumber: String): String {
        _authError.value = null
        return repository.requestOtp(phoneNumber)
    }

    fun verifyOtp(phoneNumber: String, otp: String): Boolean {
        val success = repository.verifyOtp(phoneNumber, otp)
        if (!success) {
            _authError.value = "کد تأیید وارد شده نامعتبر است."
        } else {
            _authError.value = null
        }
        return success
    }

    fun toggleAdminMode(enable: Boolean) {
        repository.toggleAdminMode(enable)
    }

    // Admin actions
    fun updateOrderStatus(orderId: Long, newStatus: OrderStatus) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
        }
    }

    fun saveProduct(product: Product) {
        viewModelScope.launch {
            repository.saveOrUpdateProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }
}

data class PendingCheckoutInfo(
    val customerName: String,
    val customerPhone: String,
    val province: String,
    val city: String,
    val address: String,
    val postalCode: String,
    val orderNotes: String,
    val shippingMethod: String,
    val paymentMethod: String,
    val items: List<CartItem>,
    val shippingCost: Long,
    val discount: Long
)
