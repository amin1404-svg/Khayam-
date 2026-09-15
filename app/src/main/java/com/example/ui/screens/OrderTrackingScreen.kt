package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.HoneyMediumBrown
import com.example.ui.theme.HoneyRedAccent
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneySurfaceVariant
import com.example.ui.theme.HoneyTextSecondary
import com.example.util.PersianUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    order: Order,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        OrderStatus.SUBMITTED to "سفارش شما در سیستم ثبت و به زنبوردار ارجاع داده شد.",
        OrderStatus.PREPARING to "عسل تازه از انبار کندوها با دقت در حال بسته‌بندی ضدضربه است.",
        OrderStatus.HANDED_OVER to "مرسوله تحویل شرکت حمل و نقل پستی گردید.",
        OrderStatus.SHIPPED to "بسته در مسیر انتقال به مقصد می‌باشد.",
        OrderStatus.DELIVERED to "سفارش با موفقیت تحویل خریدار محترم گردید."
    )

    val currentStepIndex = order.status.stepIndex

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "رهگیری سفارش ${PersianUtils.toPersianDigits(order.orderNumber)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = HoneyDarkBrown
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HoneySurfaceCard)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(HoneyCreamBg)
                .testTag("order_tracking_scroll"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Order Status Stepper Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "مراحل ارسال سفارش",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )

                            Box(
                                modifier = Modifier
                                    .background(HoneyGreenLight, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = order.status.titleFa,
                                    color = HoneyGreenAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Vertical Stepper
                        steps.forEachIndexed { index, (statusEnum, desc) ->
                            val isCompleted = currentStepIndex >= index
                            val isCurrent = currentStepIndex == index

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                // Step indicator circle & connecting line
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    isCompleted -> HoneyGreenAccent
                                                    isCurrent -> HoneyGoldPrimary
                                                    else -> HoneySurfaceVariant
                                                }
                                            )
                                            .border(
                                                1.5.dp,
                                                if (isCompleted || isCurrent) Color.White else HoneyBorder,
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isCompleted) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        } else {
                                            Text(
                                                text = PersianUtils.toPersianDigits((index + 1).toString()),
                                                color = if (isCurrent) Color.White else HoneyTextSecondary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    if (index < steps.lastIndex) {
                                        Box(
                                            modifier = Modifier
                                                .width(2.dp)
                                                .height(38.dp)
                                                .background(
                                                    if (currentStepIndex > index) HoneyGreenAccent else HoneyBorder
                                                )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                // Step details
                                Column(modifier = Modifier.padding(bottom = if (index < steps.lastIndex) 16.dp else 0.dp)) {
                                    Text(
                                        text = statusEnum.titleFa,
                                        fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isCurrent || isCompleted) HoneyDarkBrown else HoneyTextSecondary,
                                        fontSize = 13.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = desc,
                                        fontSize = 11.sp,
                                        color = if (isCurrent) HoneyMediumBrown else HoneyTextSecondary,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Shipping & Tracking Details Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, tint = HoneyGoldPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "اطلاعات پستی و ارسال",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "کد رهگیری مرسوله:", fontSize = 12.sp, color = HoneyTextSecondary)
                            Text(
                                text = PersianUtils.toPersianDigits(order.trackingCode),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = HoneyGoldPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "روش ارسال:", fontSize = 12.sp, color = HoneyTextSecondary)
                            Text(text = order.shippingMethod, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = HoneyDarkBrown)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "گیرنده:", fontSize = 12.sp, color = HoneyTextSecondary)
                            Text(text = "${order.customerName} (${order.customerPhone})", fontSize = 12.sp, color = HoneyDarkBrown)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "آدرس تحویل:", fontSize = 12.sp, color = HoneyTextSecondary)
                            Text(
                                text = "${order.province}، ${order.city}، ${order.address}",
                                fontSize = 12.sp,
                                color = HoneyDarkBrown,
                                modifier = Modifier.fillMaxWidth(0.65f)
                            )
                        }

                        if (order.orderNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "یادداشت خریدار: ${order.orderNotes}", fontSize = 11.sp, color = HoneyMediumBrown)
                        }
                    }
                }
            }

            // 3. Invoice and Items breakdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Payments, contentDescription = null, tint = HoneyGoldPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "فاکتور سفارش",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "اقلام: ${order.itemsSummary}",
                            style = MaterialTheme.typography.bodySmall,
                            color = HoneyDarkBrown,
                            lineHeight = 20.sp
                        )

                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = HoneyBorder)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "جمع محصولات:", fontSize = 12.sp, color = HoneyTextSecondary)
                            Text(text = PersianUtils.formatPrice(order.itemsSubtotalTomans), fontSize = 12.sp, color = HoneyDarkBrown)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "هزینه ارسال:", fontSize = 12.sp, color = HoneyTextSecondary)
                            Text(
                                text = if (order.shippingCostTomans == 0L) "رایگان" else PersianUtils.formatPrice(order.shippingCostTomans),
                                fontSize = 12.sp,
                                color = HoneyDarkBrown
                            )
                        }

                        if (order.discountTomans > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "تخفیف:", fontSize = 12.sp, color = HoneyRedAccent)
                                Text(text = "- ${PersianUtils.formatPrice(order.discountTomans)}", fontSize = 12.sp, color = HoneyRedAccent)
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = HoneyBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "مبلغ کل پرداختی:", fontWeight = FontWeight.Bold, color = HoneyDarkBrown)
                            Text(
                                text = PersianUtils.formatPrice(order.totalPayableTomans),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = HoneyGoldPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
