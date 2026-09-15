package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.ui.theme.HoneyBorder
import com.example.ui.theme.HoneyCreamBg
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyGreenAccent
import com.example.ui.theme.HoneyGreenLight
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneySurfaceVariant
import com.example.ui.theme.HoneyTextSecondary
import com.example.util.PersianUtils

@Composable
fun OrderSuccessScreen(
    order: Order,
    onTrackOrder: (Long) -> Unit,
    onGoHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HoneyCreamBg)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("order_success_root"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Large Success Checkmark Circle
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(HoneyGreenLight, CircleShape)
                .border(2.dp, HoneyGreenAccent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "موفقیت",
                tint = HoneyGreenAccent,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "سفارش شما با موفقیت ثبت شد",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = HoneyDarkBrown,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "از اعتماد شما به عسل اصیل ساوالان سپاسگزاریم. زنبورستان ما در حال آماده‌سازی و ارسال سفارش شماست.",
            style = MaterialTheme.typography.bodyMedium,
            color = HoneyTextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Order Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "شماره سفارش:", color = HoneyTextSecondary, fontSize = 13.sp)
                    Text(
                        text = PersianUtils.toPersianDigits(order.orderNumber),
                        fontWeight = FontWeight.Bold,
                        color = HoneyGoldPrimary,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "کد رهگیری پستی:", color = HoneyTextSecondary, fontSize = 13.sp)
                    Text(
                        text = PersianUtils.toPersianDigits(order.trackingCode),
                        fontWeight = FontWeight.SemiBold,
                        color = HoneyDarkBrown,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "زمان تقریبی تحویل:", color = HoneyTextSecondary, fontSize = 13.sp)
                    Text(
                        text = "۲ الی ۳ روز کاری",
                        fontWeight = FontWeight.SemiBold,
                        color = HoneyGreenAccent,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "روش ارسال:", color = HoneyTextSecondary, fontSize = 13.sp)
                    Text(
                        text = order.shippingMethod,
                        fontWeight = FontWeight.Medium,
                        color = HoneyDarkBrown,
                        fontSize = 12.sp
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = HoneyBorder)

                Text(
                    text = "خلاصه اقلام سفارش:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = HoneyDarkBrown
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = order.itemsSummary,
                    fontSize = 12.sp,
                    color = HoneyTextSecondary,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "مبلغ کل پرداختی:", fontWeight = FontWeight.Bold, color = HoneyDarkBrown)
                    Text(
                        text = PersianUtils.formatPrice(order.totalPayableTomans),
                        fontWeight = FontWeight.ExtraBold,
                        color = HoneyGoldPrimary,
                        fontSize = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Action Buttons
        Button(
            onClick = { onTrackOrder(order.id) },
            colors = ButtonDefaults.buttonColors(
                containerColor = HoneyGoldPrimary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_track_order_success")
        ) {
            Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "مشاهده و پیگیری سفارش", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onGoHome,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, HoneyGoldPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_go_home_success")
        ) {
            Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = HoneyGoldPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "بازگشت به صفحه اصلی", color = HoneyGoldPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
