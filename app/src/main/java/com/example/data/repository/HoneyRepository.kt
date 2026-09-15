package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.model.CartItem
import com.example.data.model.ContactMessage
import com.example.data.model.NotificationItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.Product
import com.example.data.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class HoneyRepository(private val database: AppDatabase) {

    private val productDao = database.productDao()
    private val cartDao = database.cartDao()
    private val orderDao = database.orderDao()
    private val notificationDao = database.notificationDao()
    private val contactDao = database.contactDao()

    // Current logged-in user profile
    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "usr_savalan_1",
            fullName = "امین رضایی",
            phoneNumber = "۰۹۱۲۳۴۵۶۷۸۹",
            email = "customer@savalanhoney.ir",
            isVerified = true,
            isAdmin = false,
            loyaltyPoints = 180
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // Mock OTP verification state
    private val _lastGeneratedOtp = MutableStateFlow<String?>(null)
    val lastGeneratedOtp: StateFlow<String?> = _lastGeneratedOtp.asStateFlow()

    val allActiveProducts: Flow<List<Product>> = productDao.getAllActiveProducts()
    val allAdminProducts: Flow<List<Product>> = productDao.getAllProductsForAdmin()
    val favoriteProducts: Flow<List<Product>> = productDao.getFavoriteProducts()
    val cartItems: Flow<List<CartItem>> = cartDao.getAllCartItems()
    val allOrders: Flow<List<Order>> = orderDao.getAllOrders()
    val notifications: Flow<List<NotificationItem>> = notificationDao.getAllNotifications()
    val unreadNotificationsCount: Flow<Int> = notificationDao.getUnreadCount()
    val contactMessages: Flow<List<ContactMessage>> = contactDao.getAllMessages()

    suspend fun initializeSeedDataIfNeeded() = withContext(Dispatchers.IO) {
        val count = productDao.getProductsCount()
        if (count == 0) {
            val sampleProducts = listOf(
                Product(
                    id = 1,
                    title = "عسل طبیعی ساوالان – ۵۰۰ گرم",
                    subtitle = "برداشت خالص از مراتع بکر دامنه سبلان",
                    description = "عسل صددرصد طبیعی و خام، مستقیماً از کندوهای مستقر در دامنه‌های سرسبز کوهستان سبلان در ارتفاع ۲۵۰۰ متری. بدون هرگونه فرآوری حرارتی و با حفظ تمامی آنزیم‌ها، ویتامین‌ها و عطر اصیل گل‌های کوهستانی.",
                    honeyType = "عسل طبیعی",
                    availableWeightsCsv = "250,500,1000,2000",
                    defaultWeightGrams = 500,
                    basePriceTomans = 340000,
                    discountPercent = 10,
                    stockQuantity = 45,
                    isBestSeller = true,
                    isSpecialOffer = true,
                    imageDrawableName = "img_honey_natural",
                    harvestRegion = "دامنه‌های کوه سبلان (ساوالان)",
                    sucrosePercentage = 1.4,
                    purityPercentage = 100,
                    healthBenefits = "تقویت فوق‌العاده سیستم ایمنی، تسکین سرفه و گلودرد، انرژی‌زای طبیعی و بهبود سلامت گوارش."
                ),
                Product(
                    id = 2,
                    title = "عسل طبیعی ساوالان – ۱ کیلوگرم",
                    subtitle = "بسته‌بندی اقتصادی شیشه‌ای برای خانواده",
                    description = "شیشه یک کیلوگرمی عسل طبیعی کوهستان سبلan با عطر و طعم بسیار ملایم و ماندگار. سرشار از پلی‌فنول‌ها و آنتی‌اکسیدان‌های طبیعی حاصل از شهد شهدزایان وحشی دامنه آتشفشانی سبلان.",
                    honeyType = "عسل طبیعی",
                    availableWeightsCsv = "500,1000,2000",
                    defaultWeightGrams = 1000,
                    basePriceTomans = 340000, // base is 500g, 1000g gets 0.95 factor
                    discountPercent = 0,
                    stockQuantity = 30,
                    isBestSeller = true,
                    isSpecialOffer = false,
                    imageDrawableName = "img_honey_natural",
                    harvestRegion = "دامنه‌های کوه سبلان",
                    sucrosePercentage = 1.5,
                    purityPercentage = 100,
                    healthBenefits = "تغذیه کامل سلولی، بهبود خواب آرام شبانه و تقویت عمومی بنیه بدن."
                ),
                Product(
                    id = 3,
                    title = "عسل گون ساوالان – ۵۰۰ گرم",
                    subtitle = "دارویی، غنی از آنتی‌اکسیدان و تسکین‌دهنده معده",
                    description = "عسل تک‌گل گون خالص با رنگ کهربایی روشن، برگرفته از دشت‌های وسیع گون‌زارهای ییلاقات مشگین‌شهر و سرعین. ساکارز بسیار پایین و مناسب حتی برای افرادی که مصرف قند کنترل‌شده دارند.",
                    honeyType = "عسل گون",
                    availableWeightsCsv = "250,500,1000",
                    defaultWeightGrams = 500,
                    basePriceTomans = 390000,
                    discountPercent = 15,
                    stockQuantity = 28,
                    isBestSeller = true,
                    isSpecialOffer = true,
                    imageDrawableName = "img_honey_gavan",
                    harvestRegion = "ییلاقات گون‌خیز ساوالان",
                    sucrosePercentage = 1.1,
                    purityPercentage = 100,
                    healthBenefits = "تسکین دردهای معده و ریفلاکس، ضد استرس و اضطراب و محافظت از کبد."
                ),
                Product(
                    id = 4,
                    title = "عسل آویشن ساوالان – ۵۰۰ گرم",
                    subtitle = "عطر بی‌نظیر گیاهی با خواص ضدباکتریایی قوی",
                    description = "عسل پرطرفدار آویشن کوهی با رنگ کهربایی تیره و رایحه دلپذیر آویشن وحشی. برداشت شده در اوج شکوفایی بوته‌های آویشن دامنه‌های صخره‌ای قله سلطان سبلان.",
                    honeyType = "عسل آویشن",
                    availableWeightsCsv = "250,500,1000",
                    defaultWeightGrams = 500,
                    basePriceTomans = 420000,
                    discountPercent = 8,
                    stockQuantity = 22,
                    isBestSeller = false,
                    isSpecialOffer = true,
                    imageDrawableName = "img_honey_thyme",
                    harvestRegion = "صخره‌های آویشن‌خیز سبلان",
                    sucrosePercentage = 1.2,
                    purityPercentage = 100,
                    healthBenefits = "قوی‌ترین آنتی‌بیوتیک طبیعی، ضدعفونی‌کننده مجاری تنفسی و تسکین دردهای مفصلی."
                ),
                Product(
                    id = 5,
                    title = "عسل چندگیاه ساوالان – ۱ کیلوگرم",
                    subtitle = "ترکیب معجزه‌آسا از شهد صدها گل و گیاه دارویی",
                    description = "عسل چهل‌گیاه ساوالان ترکیبی معطر از گون، بابونه، آویشن، کاسنی، بومادران و شقایق کوهی. تعادل فوق‌العاده در طعم، رنگ و خواص درمانی چندگانه.",
                    honeyType = "عسل چندگیاه",
                    availableWeightsCsv = "500,1000,2000",
                    defaultWeightGrams = 1000,
                    basePriceTomans = 310000,
                    discountPercent = 12,
                    stockQuantity = 55,
                    isBestSeller = true,
                    isSpecialOffer = false,
                    imageDrawableName = "img_hero_banner",
                    harvestRegion = "چمنزارهای مرتفع سبلان",
                    sucrosePercentage = 1.8,
                    purityPercentage = 100,
                    healthBenefits = "تقویت حافظه و تمرکز، تصفیه خون و نشاط‌آور طبیعی."
                ),
                Product(
                    id = 6,
                    title = "عسل با موم طبیعی ساوالان – ۱ کیلوگرم",
                    subtitle = "شان کامل موم طبیعی با رایحه گل‌های وحشی",
                    description = "برش مستقیم از شان عسل با موم ارگانیک ساخته شده توسط خود زنبورها بدون برگ موم مصنوعی. جویدن موم طبیعی برای بهبود سینوزیت و سلامت دندان‌ها بی‌نظیر است.",
                    honeyType = "محصولات ویژه",
                    availableWeightsCsv = "1000,2000",
                    defaultWeightGrams = 1000,
                    basePriceTomans = 450000,
                    discountPercent = 0,
                    stockQuantity = 15,
                    isBestSeller = false,
                    isSpecialOffer = true,
                    imageDrawableName = "img_honey_natural",
                    harvestRegion = "زنبورستان مرکزی ساوالان",
                    sucrosePercentage = 1.3,
                    purityPercentage = 100,
                    healthBenefits = "تسکین آلرژی فصلی، پاکسازی سینوس‌ها و جویدن موم جهت سلامت لثه."
                )
            )
            productDao.insertProducts(sampleProducts)

            // Seed initial notifications
            notificationDao.insertNotification(
                NotificationItem(
                    title = "به عسل ساوالان خوش آمدید!",
                    message = "از خرید مستقیم عسل ناب دامنه‌های سبلان با ضمانت ۱۰۰٪ آزمایشگاهی لذت ببرید.",
                    dateText = "امروز",
                    type = "brand",
                    isRead = false
                )
            )
            notificationDao.insertNotification(
                NotificationItem(
                    title = "تخفیف ویژه برداشت تازه عسل گون",
                    message = "تا ۱۵٪ تخفیف روی عسل گون در شیشه‌های ۵۰۰ گرمی به مدت محدود فعال شد.",
                    dateText = "دیروز",
                    type = "discount",
                    isRead = false
                )
            )
        }
    }

    fun getProductById(id: Long): Flow<Product?> = productDao.getProductById(id)

    suspend fun getProductByIdSync(id: Long): Product? = productDao.getProductByIdSync(id)

    suspend fun toggleFavorite(product: Product) = withContext(Dispatchers.IO) {
        val newFav = !product.isFavorite
        productDao.setFavorite(product.id, newFav)
    }

    // Cart methods
    suspend fun addToCart(product: Product, weightGrams: Int, quantity: Int = 1) = withContext(Dispatchers.IO) {
        val existing = cartDao.findCartItem(product.id, weightGrams)
        val unitPrice = product.getDiscountedPrice(weightGrams)
        if (existing != null) {
            cartDao.updateCartItem(existing.copy(quantity = existing.quantity + quantity))
        } else {
            val newItem = CartItem(
                productId = product.id,
                productTitle = product.title,
                selectedWeightGrams = weightGrams,
                unitPriceTomans = unitPrice,
                quantity = quantity,
                imageDrawableName = product.imageDrawableName
            )
            cartDao.insertCartItem(newItem)
        }
    }

    suspend fun updateCartItemQuantity(cartItem: CartItem, newQuantity: Int) = withContext(Dispatchers.IO) {
        if (newQuantity <= 0) {
            cartDao.deleteCartItem(cartItem)
        } else {
            cartDao.updateCartItem(cartItem.copy(quantity = newQuantity))
        }
    }

    suspend fun removeCartItem(cartItem: CartItem) = withContext(Dispatchers.IO) {
        cartDao.deleteCartItem(cartItem)
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        cartDao.clearCart()
    }

    // Order Creation
    suspend fun createOrder(
        customerName: String,
        customerPhone: String,
        province: String,
        city: String,
        address: String,
        postalCode: String,
        orderNotes: String,
        shippingMethod: String,
        paymentMethod: String,
        items: List<CartItem>,
        shippingCostTomans: Long,
        discountTomans: Long
    ): Order = withContext(Dispatchers.IO) {
        val subtotal = items.sumOf { it.totalPriceTomans }
        val total = (subtotal + shippingCostTomans - discountTomans).coerceAtLeast(0)
        val orderNum = "SAV-${10000 + Random.nextInt(100, 9999)}"
        val dateString = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
        val summary = items.joinToString("، ") { "${it.productTitle} (${it.quantity} عدد)" }

        val order = Order(
            orderNumber = orderNum,
            orderDate = dateString,
            customerName = customerName,
            customerPhone = customerPhone,
            province = province,
            city = city,
            address = address,
            postalCode = postalCode,
            orderNotes = orderNotes,
            itemsSummary = summary,
            totalItemsCount = items.sumOf { it.quantity },
            itemsSubtotalTomans = subtotal,
            shippingCostTomans = shippingCostTomans,
            discountTomans = discountTomans,
            totalPayableTomans = total,
            shippingMethod = shippingMethod,
            paymentMethod = paymentMethod,
            status = OrderStatus.SUBMITTED,
            trackingCode = "TRK-${Random.nextInt(100000, 999999)}",
            paymentTransactionId = "TX-${Random.nextInt(10000000, 99999999)}"
        )

        val newId = orderDao.insertOrder(order)
        // Clear cart after successful order creation
        cartDao.clearCart()

        // Insert notification
        notificationDao.insertNotification(
            NotificationItem(
                title = "سفارش شما با موفقیت ثبت شد",
                message = "سفارش $orderNum ثبت شد و در حال پردازش توسط زنبوردار است.",
                dateText = "امروز",
                type = "order",
                isRead = false
            )
        )

        order.copy(id = newId)
    }

    fun getOrderById(orderId: Long): Flow<Order?> = orderDao.getOrderById(orderId)

    fun getOrderByNumber(orderNumber: String): Flow<Order?> = orderDao.getOrderByNumber(orderNumber)

    suspend fun updateOrderStatus(orderId: Long, newStatus: OrderStatus) = withContext(Dispatchers.IO) {
        orderDao.updateOrderStatus(orderId, newStatus)
    }

    // Contact messages
    suspend fun sendContactMessage(name: String, phone: String, subject: String, message: String): Long = withContext(Dispatchers.IO) {
        val dateString = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date())
        val contact = ContactMessage(
            senderName = name,
            phoneNumber = phone,
            subject = subject,
            messageText = message,
            dateText = dateString
        )
        contactDao.insertMessage(contact)
    }

    // Notification operations
    suspend fun markAllNotificationsAsRead() = withContext(Dispatchers.IO) {
        notificationDao.markAllAsRead()
    }

    // Admin Operations
    suspend fun saveOrUpdateProduct(product: Product) = withContext(Dispatchers.IO) {
        if (product.id == 0L) {
            productDao.insertProduct(product)
        } else {
            productDao.updateProduct(product)
        }
    }

    suspend fun deleteProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.deleteProduct(product)
    }

    suspend fun getAdminOrdersCount(): Int = withContext(Dispatchers.IO) {
        orderDao.getOrdersCount()
    }

    suspend fun getAdminTotalSales(): Long = withContext(Dispatchers.IO) {
        orderDao.getTotalSalesTomans() ?: 0L
    }

    // Auth & OTP simulation
    fun requestOtp(phoneNumber: String): String {
        val otp = (100000 + Random.nextInt(900000)).toString()
        _lastGeneratedOtp.value = otp
        return otp
    }

    fun verifyOtp(phoneNumber: String, enteredOtp: String): Boolean {
        // Accept generated OTP or fallback 123456 for easy developer testing
        val valid = enteredOtp == _lastGeneratedOtp.value || enteredOtp == "123456"
        if (valid) {
            _currentUser.value = _currentUser.value.copy(
                phoneNumber = phoneNumber,
                isVerified = true
            )
            _lastGeneratedOtp.value = null
        }
        return valid
    }

    fun toggleAdminMode(enableAdmin: Boolean) {
        _currentUser.value = _currentUser.value.copy(isAdmin = enableAdmin)
    }
}
