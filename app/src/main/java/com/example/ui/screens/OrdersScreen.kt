package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.ui.theme.HoneyBorder
import com.example.ui.theme.HoneyCreamBg
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyGreenAccent
import com.example.ui.theme.HoneyGreenLight
import com.example.ui.theme.HoneyRedAccent
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneySurfaceVariant
import com.example.ui.theme.HoneyTextSecondary
import com.example.util.PersianUtils

@Composable
fun OrdersScreen(
    orders: List<Order>,
    onOrderClick: (Long) -> Unit,
    onStartShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchOrderQuery by remember { mutableStateOf("") }

    val filteredOrders = if (searchOrderQuery.isBlank()) {
        orders
    } else {
        orders.filter {
            it.orderNumber.contains(searchOrderQuery.trim(), ignoreCase = true) ||
            it.customerPhone.contains(searchOrderQuery.trim())
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HoneyCreamBg)
            .testTag("orders_screen_root")
    ) {
        // Search by Order Number
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchOrderQuery,
                onValueChange = { searchOrderQuery = it },
                label = { Text("پیگیری با شماره سفارش (مثال: SAV-10024)", fontSize = 12.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = HoneyGoldPrimary)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("order_search_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HoneyGoldPrimary,
                    unfocusedBorderColor = HoneyBorder,
                    focusedContainerColor = HoneySurfaceCard,
                    unfocusedContainerColor = HoneySurfaceCard
                )
            )
        }

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(HoneySurfaceVariant, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = "بدون سفارش",
                            tint = HoneyGoldPrimary,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "هنوز سفارشی ثبت نکرده‌اید",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "از بخش فروشگاه، عسل‌های طبیعی و ارگانیک سبلان را انتخاب و سفارش دهید.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = HoneyTextSecondary,
                        fontSize = 13.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onStartShopping,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HoneyGoldPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("مشاهده محصولات عسل", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredOrders, key = { it.id }) { order ->
                    OrderCardItem(
                        order = order,
                        onClick = { onOrderClick(order.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCardItem(
    order: Order,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("order_card_${order.id}"),
        colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "سفارش ${PersianUtils.toPersianDigits(order.orderNumber)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = HoneyDarkBrown
                )

                // Status Pill
                val (statusBg, statusFg) = when (order.status) {
                    OrderStatus.DELIVERED -> HoneyGreenLight to HoneyGreenAccent
                    OrderStatus.CANCELLED -> Color(0xFFFFE4E6) to HoneyRedAccent
                    else -> HoneySurfaceVariant to HoneyGoldPrimary
                }

                Box(
                    modifier = Modifier
                        .background(statusBg, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = order.status.titleFa,
                        color = statusFg,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = order.itemsSummary,
                style = MaterialTheme.typography.bodySmall,
                color = HoneyTextSecondary,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Divider(color = HoneyBorder.copy(alpha = 0.5f))

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "تاریخ: ${PersianUtils.toPersianDigits(order.orderDate)}",
                        fontSize = 11.sp,
                        color = HoneyTextSecondary
                    )
                    Text(
                        text = PersianUtils.formatPrice(order.totalPayableTomans),
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown,
                        fontSize = 13.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(HoneySurfaceVariant, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "رهگیری و جزئیات",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = HoneyGoldPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = HoneyGoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
