package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.HoneyBorder
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyGoldSecondary
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneyTextSecondary
import com.example.util.PersianUtils

@Composable
fun AppHeader(
    cartCount: Int,
    unreadNotificationsCount: Int,
    onCartClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = HoneySurfaceCard,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Titles
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.5.dp, HoneyGoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon),
                        contentDescription = "عسل ساوالان",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "عسل ساوالان",
                        style = MaterialTheme.typography.titleMedium,
                        color = HoneyDarkBrown,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "«عطر طبیعت، طعم اصالت»",
                        style = MaterialTheme.typography.labelSmall,
                        color = HoneyTextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            // Actions: Notifications & Cart
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Notification Button with badge
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier.testTag("notification_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadNotificationsCount > 0) {
                                Badge(
                                    containerColor = HoneyGoldPrimary,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = PersianUtils.toPersianDigits(unreadNotificationsCount.toString()),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "اعلان‌ها",
                            tint = HoneyDarkBrown,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Cart Button with badge
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier.testTag("cart_header_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge(
                                    containerColor = HoneyGoldSecondary,
                                    contentColor = HoneyDarkBrown
                                ) {
                                    Text(
                                        text = PersianUtils.toPersianDigits(cartCount.toString()),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "سبد خرید",
                            tint = HoneyDarkBrown,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
