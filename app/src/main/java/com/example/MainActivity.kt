package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.components.AppHeader
import com.example.ui.components.SavalanBottomBar
import com.example.ui.navigation.NavScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.ContactProducerScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OrderSuccessScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.PaymentGatewayDialog
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.HoneyCreamBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.HoneyViewModel
import com.example.util.RtlProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                RtlProvider {
                    SavalanHoneyApp()
                }
            }
        }
    }
}

@Composable
fun SavalanHoneyApp(
    viewModel: HoneyViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // ViewModel states
    val allProducts by viewModel.allProducts.collectAsState()
    val adminProducts by viewModel.adminProducts.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val favoriteProducts by viewModel.favoriteProducts.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val totalCartCount by viewModel.totalCartCount.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val unreadNotificationsCount by viewModel.unreadNotificationsCount.collectAsState()
    val contactMessages by viewModel.contactMessages.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedSort by viewModel.sortBy.collectAsState()
    val selectedWeightFilter by viewModel.selectedWeightFilter.collectAsState()
    val appliedCoupon by viewModel.appliedCouponCode.collectAsState()
    val couponDiscount by viewModel.couponDiscountAmount.collectAsState()
    val isPaymentSheetOpen by viewModel.isPaymentSheetOpen.collectAsState()
    val pendingOrderData by viewModel.pendingOrderData.collectAsState()
    val lastPlacedOrder by viewModel.lastPlacedOrder.collectAsState()
    val lastGeneratedOtp by viewModel.lastGeneratedOtp.collectAsState()
    val authError by viewModel.authError.collectAsState()

    // Handle order completion navigation
    LaunchedEffect(lastPlacedOrder) {
        lastPlacedOrder?.let { order ->
            navController.navigate(NavScreen.OrderSuccess.createRoute(order.id)) {
                popUpTo(NavScreen.Cart.route) { inclusive = true }
            }
        }
    }

    val isMainTab = currentRoute in listOf(
        NavScreen.Home.route,
        NavScreen.Shop.route,
        NavScreen.Cart.route,
        NavScreen.Orders.route,
        NavScreen.Profile.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isMainTab) {
                AppHeader(
                    cartCount = totalCartCount,
                    unreadNotificationsCount = unreadNotificationsCount,
                    onCartClick = {
                        if (currentRoute != NavScreen.Cart.route) {
                            navController.navigate(NavScreen.Cart.route)
                        }
                    },
                    onNotificationsClick = {
                        navController.navigate(NavScreen.Notifications.route)
                    }
                )
            }
        },
        bottomBar = {
            if (isMainTab) {
                SavalanBottomBar(
                    currentRoute = currentRoute,
                    cartCount = totalCartCount,
                    onNavigate = { targetRoute ->
                        navController.navigate(targetRoute) {
                            popUpTo(NavScreen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(HoneyCreamBg)
        ) {
            NavHost(
                navController = navController,
                startDestination = NavScreen.Splash.route
            ) {
                // Splash Screen
                composable(NavScreen.Splash.route) {
                    SplashScreen(
                        onSplashFinished = {
                            navController.navigate(NavScreen.Home.route) {
                                popUpTo(NavScreen.Splash.route) { inclusive = true }
                            }
                        }
                    )
                }

                // 1. Home Screen
                composable(NavScreen.Home.route) {
                    HomeScreen(
                        products = allProducts,
                        onProductClick = { productId ->
                            navController.navigate(NavScreen.ProductDetail.createRoute(productId))
                        },
                        onAddToCart = { product ->
                            viewModel.addToCart(product)
                        },
                        onToggleFavorite = { product ->
                            viewModel.toggleFavorite(product)
                        },
                        onNavigateToShop = { category ->
                            viewModel.setCategory(category)
                            navController.navigate(NavScreen.Shop.route)
                        },
                        onNavigateToContact = {
                            navController.navigate(NavScreen.ContactProducer.route)
                        }
                    )
                }

                // 2. Shop Screen
                composable(NavScreen.Shop.route) {
                    ShopScreen(
                        products = filteredProducts,
                        searchQuery = searchQuery,
                        selectedCategory = selectedCategory,
                        selectedSort = selectedSort,
                        selectedWeight = selectedWeightFilter,
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onCategoryChange = { viewModel.setCategory(it) },
                        onSortChange = { viewModel.setSortBy(it) },
                        onWeightChange = { viewModel.setWeightFilter(it) },
                        onProductClick = { productId ->
                            navController.navigate(NavScreen.ProductDetail.createRoute(productId))
                        },
                        onAddToCart = { product ->
                            viewModel.addToCart(product)
                        },
                        onToggleFavorite = { product ->
                            viewModel.toggleFavorite(product)
                        }
                    )
                }

                // 3. Product Detail Screen
                composable(
                    route = NavScreen.ProductDetail.route,
                    arguments = listOf(navArgument("productId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
                    val product = allProducts.find { it.id == productId }
                    if (product != null) {
                        ProductDetailScreen(
                            product = product,
                            onBack = { navController.popBackStack() },
                            onAddToCart = { prod, weight, qty ->
                                viewModel.addToCart(prod, weight, qty)
                            },
                            onQuickBuy = { prod, weight, qty ->
                                viewModel.addToCart(prod, weight, qty)
                                navController.navigate(NavScreen.Checkout.route)
                            },
                            onToggleFavorite = { prod ->
                                viewModel.toggleFavorite(prod)
                            },
                            onContactProducerAboutProduct = {
                                navController.navigate(NavScreen.ContactProducer.route)
                            }
                        )
                    }
                }

                // 4. Cart Screen
                composable(NavScreen.Cart.route) {
                    CartScreen(
                        cartItems = cartItems,
                        appliedCoupon = appliedCoupon,
                        couponDiscount = couponDiscount,
                        onQuantityChange = { item, newQty ->
                            viewModel.updateCartQuantity(item, newQty)
                        },
                        onRemoveItem = { item ->
                            viewModel.removeCartItem(item)
                        },
                        onApplyCoupon = { code ->
                            viewModel.applyCoupon(code)
                        },
                        onRemoveCoupon = {
                            viewModel.removeCoupon()
                        },
                        onProceedToCheckout = {
                            navController.navigate(NavScreen.Checkout.route)
                        },
                        onExploreShop = {
                            navController.navigate(NavScreen.Shop.route)
                        }
                    )
                }

                // 5. Checkout Screen
                composable(NavScreen.Checkout.route) {
                    CheckoutScreen(
                        cartItems = cartItems,
                        couponDiscount = couponDiscount,
                        onBack = { navController.popBackStack() },
                        onSubmitCheckout = { name, phone, province, city, address, postal, notes, shipping, payment, shippingCost ->
                            viewModel.initiateCheckout(
                                customerName = name,
                                customerPhone = phone,
                                province = province,
                                city = city,
                                address = address,
                                postalCode = postal,
                                orderNotes = notes,
                                shippingMethod = shipping,
                                paymentMethod = payment,
                                shippingCost = shippingCost
                            )
                        }
                    )
                }

                // 6. Order Success Screen
                composable(
                    route = NavScreen.OrderSuccess.route,
                    arguments = listOf(navArgument("orderId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getLong("orderId") ?: 0L
                    val order = orders.find { it.id == orderId } ?: lastPlacedOrder
                    if (order != null) {
                        OrderSuccessScreen(
                            order = order,
                            onTrackOrder = { id ->
                                navController.navigate(NavScreen.OrderTracking.createRoute(id))
                            },
                            onGoHome = {
                                navController.navigate(NavScreen.Home.route) {
                                    popUpTo(NavScreen.Home.route) { inclusive = true }
                                }
                            }
                        )
                    }
                }

                // 7. Orders Screen
                composable(NavScreen.Orders.route) {
                    OrdersScreen(
                        orders = orders,
                        onOrderClick = { orderId ->
                            navController.navigate(NavScreen.OrderTracking.createRoute(orderId))
                        },
                        onStartShopping = {
                            navController.navigate(NavScreen.Shop.route)
                        }
                    )
                }

                // 8. Order Tracking Screen
                composable(
                    route = NavScreen.OrderTracking.route,
                    arguments = listOf(navArgument("orderId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getLong("orderId") ?: 0L
                    val order = orders.find { it.id == orderId }
                    if (order != null) {
                        OrderTrackingScreen(
                            order = order,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                // 9. Profile Screen
                composable(NavScreen.Profile.route) {
                    ProfileScreen(
                        userProfile = currentUser,
                        onNavigateOrders = { navController.navigate(NavScreen.Orders.route) },
                        onNavigateFavorites = { navController.navigate(NavScreen.Favorites.route) },
                        onNavigateNotifications = { navController.navigate(NavScreen.Notifications.route) },
                        onNavigateContactProducer = { navController.navigate(NavScreen.ContactProducer.route) },
                        onNavigateAuth = { navController.navigate(NavScreen.Auth.route) },
                        onNavigateAdmin = { navController.navigate(NavScreen.AdminDashboard.route) },
                        onToggleAdminMode = { viewModel.toggleAdminMode(it) }
                    )
                }

                // 10. Auth Screen
                composable(NavScreen.Auth.route) {
                    AuthScreen(
                        currentPhone = currentUser.phoneNumber,
                        lastGeneratedOtp = lastGeneratedOtp,
                        authError = authError,
                        onRequestOtp = { viewModel.sendOtp(it) },
                        onVerifyOtp = { phone, otp -> viewModel.verifyOtp(phone, otp) },
                        onAuthSuccess = { navController.popBackStack() },
                        onBack = { navController.popBackStack() }
                    )
                }

                // 11. Contact Producer Screen
                composable(NavScreen.ContactProducer.route) {
                    ContactProducerScreen(
                        onBack = { navController.popBackStack() },
                        onSendMessage = { name, phone, subject, message, onSent ->
                            viewModel.sendContactMessage(name, phone, subject, message, onSent)
                        }
                    )
                }

                // 12. Favorites Screen
                composable(NavScreen.Favorites.route) {
                    FavoritesScreen(
                        favoriteProducts = favoriteProducts,
                        onProductClick = { productId ->
                            navController.navigate(NavScreen.ProductDetail.createRoute(productId))
                        },
                        onAddToCart = { product ->
                            viewModel.addToCart(product)
                        },
                        onToggleFavorite = { product ->
                            viewModel.toggleFavorite(product)
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                // 13. Notifications Screen
                composable(NavScreen.Notifications.route) {
                    NotificationsScreen(
                        notifications = notifications,
                        onMarkAllAsRead = { viewModel.markAllNotificationsAsRead() },
                        onBack = { navController.popBackStack() }
                    )
                }

                // 14. Admin Dashboard Screen
                composable(NavScreen.AdminDashboard.route) {
                    AdminDashboardScreen(
                        orders = orders,
                        products = adminProducts,
                        contactMessages = contactMessages,
                        onUpdateOrderStatus = { id, status -> viewModel.updateOrderStatus(id, status) },
                        onSaveProduct = { viewModel.saveProduct(it) },
                        onDeleteProduct = { viewModel.deleteProduct(it) },
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            // Shaparak / Online Payment simulation modal
            if (isPaymentSheetOpen && pendingOrderData != null) {
                val payable = (pendingOrderData!!.items.sumOf { it.totalPriceTomans } +
                        pendingOrderData!!.shippingCost - pendingOrderData!!.discount).coerceAtLeast(0L)
                PaymentGatewayDialog(
                    payableAmountTomans = payable,
                    onPaymentResult = { success ->
                        viewModel.onPaymentCompleted(success)
                    },
                    onDismiss = {
                        viewModel.onPaymentCompleted(false)
                    }
                )
            }
        }
    }
}

