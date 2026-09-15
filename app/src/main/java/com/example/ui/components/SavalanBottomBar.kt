package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.navigation.NavScreen
import com.example.ui.theme.HoneyCreamBg
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyGoldSecondary
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneyTextSecondary
import com.example.util.PersianUtils

data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int = 0,
    val testTag: String
)

@Composable
fun SavalanBottomBar(
    currentRoute: String?,
    cartCount: Int,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(
            title = "خانه",
            route = NavScreen.Home.route,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            testTag = "tab_home"
        ),
        BottomNavItem(
            title = "فروشگاه",
            route = NavScreen.Shop.route,
            selectedIcon = Icons.Filled.ShoppingBag,
            unselectedIcon = Icons.Outlined.ShoppingBag,
            testTag = "tab_shop"
        ),
        BottomNavItem(
            title = "سبد خرید",
            route = NavScreen.Cart.route,
            selectedIcon = Icons.Filled.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart,
            badgeCount = cartCount,
            testTag = "tab_cart"
        ),
        BottomNavItem(
            title = "سفارش‌ها",
            route = NavScreen.Orders.route,
            selectedIcon = Icons.Filled.ReceiptLong,
            unselectedIcon = Icons.Outlined.ReceiptLong,
            testTag = "tab_orders"
        ),
        BottomNavItem(
            title = "حساب من",
            route = NavScreen.Profile.route,
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            testTag = "tab_profile"
        )
    )

    NavigationBar(
        modifier = modifier,
        containerColor = HoneySurfaceCard,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                modifier = Modifier.testTag(item.testTag),
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        onNavigate(item.route)
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount > 0) {
                                Badge(
                                    containerColor = HoneyGoldPrimary,
                                    contentColor = HoneySurfaceCard
                                ) {
                                    Text(
                                        text = PersianUtils.toPersianDigits(item.badgeCount.toString()),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = HoneyDarkBrown,
                    selectedTextColor = HoneyDarkBrown,
                    indicatorColor = HoneyGoldSecondary.copy(alpha = 0.25f),
                    unselectedIconColor = HoneyTextSecondary,
                    unselectedTextColor = HoneyTextSecondary
                )
            )
        }
    }
}
