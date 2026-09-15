package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.HoneyBorder
import com.example.ui.theme.HoneyCreamBg
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyGoldSecondary
import com.example.ui.theme.HoneyGreenAccent
import com.example.ui.theme.HoneyGreenLight
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneySurfaceVariant
import com.example.ui.theme.HoneyTextSecondary
import com.example.util.PersianUtils

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onNavigateOrders: () -> Unit,
    onNavigateFavorites: () -> Unit,
    onNavigateNotifications: () -> Unit,
    onNavigateContactProducer: () -> Unit,
    onNavigateAuth: () -> Unit,
    onNavigateAdmin: () -> Unit,
    onToggleAdminMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(HoneyCreamBg)
            .testTag("profile_screen_root"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // User Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(HoneySurfaceVariant)
                            .border(2.dp, HoneyGoldPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "کاربر",
                            tint = HoneyGoldPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userProfile.fullName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            if (userProfile.isVerified) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = "تأیید شده",
                                    tint = HoneyGreenAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = PersianUtils.toPersianDigits(userProfile.phoneNumber),
                            fontSize = 12.sp,
                            color = HoneyTextSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Customer Loyalty Points Badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(HoneySurfaceVariant, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = HoneyGoldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${PersianUtils.toPersianDigits(userProfile.loyaltyPoints.toString())} امتیاز کندوی طلایی",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )
                        }
                    }

                    // Change / Login Phone button
                    Text(
                        text = "تغییر / ورود",
                        color = HoneyGoldPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onNavigateAuth() }
                            .padding(6.dp)
                    )
                }
            }
        }

        // Account Menu Actions
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    ProfileMenuItem(
                        icon = Icons.Default.ReceiptLong,
                        title = "تاریخچه و پیگیری سفارش‌ها",
                        subtitle = "مشاهده سفارش‌های جاری و قبلی",
                        onClick = onNavigateOrders,
                        testTag = "menu_orders"
                    )

                    Divider(color = HoneyBorder.copy(alpha = 0.4f), modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileMenuItem(
                        icon = Icons.Default.Favorite,
                        title = "علاقه‌مندی‌ها",
                        subtitle = "عسل‌های برگزیده و نشان‌شده",
                        onClick = onNavigateFavorites,
                        testTag = "menu_favorites"
                    )

                    Divider(color = HoneyBorder.copy(alpha = 0.4f), modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileMenuItem(
                        icon = Icons.Default.Notifications,
                        title = "پیام‌ها و اعلان‌ها",
                        subtitle = "تخفیف‌های فصلی و اطلاعیه‌های زنبورستان",
                        onClick = onNavigateNotifications,
                        testTag = "menu_notifications"
                    )

                    Divider(color = HoneyBorder.copy(alpha = 0.4f), modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileMenuItem(
                        icon = Icons.Default.HeadsetMic,
                        title = "ارتباط با زنبوردار و تولیدکننده",
                        subtitle = "مشاوره خرید، ارسال پیام و پاسخگویی مستقیم",
                        onClick = onNavigateContactProducer,
                        testTag = "menu_contact"
                    )
                }
            }
        }

        // Saved Addresses Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = HoneyGoldPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "آدرس ثبت‌شده من",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = HoneyDarkBrown
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(HoneyGreenLight, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "پیش‌فرض",
                                color = HoneyGreenAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "تهران، خیابان سبلان شمالی، کوچه بهار، پلاک ۱۲، واحد ۴",
                        fontSize = 12.sp,
                        color = HoneyDarkBrown,
                        lineHeight = 20.sp
                    )

                    Text(
                        text = "کد پستی: ۱۶۴۵۸۹۳۲۱۱",
                        fontSize = 11.sp,
                        color = HoneyTextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Producer Admin Panel Switch (پنل مدیریت تولیدکننده)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = HoneySurfaceVariant),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, HoneyGoldPrimary.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = HoneyGoldPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "پنل اختصاصی مدیریت زنبوردار",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = HoneyDarkBrown
                                )
                                Text(
                                    text = "مدیریت سفارش‌ها، محصولات و پیام‌های مشتریان",
                                    fontSize = 11.sp,
                                    color = HoneyTextSecondary
                                )
                            }
                        }

                        Switch(
                            checked = userProfile.isAdmin,
                            onCheckedChange = { checked ->
                                onToggleAdminMode(checked)
                                if (checked) onNavigateAdmin()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = HoneyGoldPrimary
                            )
                        )
                    }

                    if (userProfile.isAdmin) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(HoneyGoldPrimary)
                                .clickable { onNavigateAdmin() }
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ورود به داشبورد مدیریت تولیدکننده",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(HoneySurfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = HoneyGoldPrimary, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = HoneyDarkBrown
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = HoneyTextSecondary
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = HoneyTextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}
