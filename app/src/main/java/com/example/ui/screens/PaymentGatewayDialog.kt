package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.HoneyBorder
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyGreenAccent
import com.example.ui.theme.HoneyRedAccent
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneySurfaceVariant
import com.example.ui.theme.HoneyTextSecondary
import com.example.util.PersianUtils

@Composable
fun PaymentGatewayDialog(
    payableAmountTomans: Long,
    onPaymentResult: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var cardNumber by remember { mutableStateOf("۶۰۳۷-۹۹۷۵-۱۲۳۴-۵۶۷۸") }
    var cvv2 by remember { mutableStateOf("۳۴۲") }
    var expireMonth by remember { mutableStateOf("۰۸") }
    var expireYear by remember { mutableStateOf("۰۶") }
    var dynamicOtp by remember { mutableStateOf("۸۷۶۵۴") }
    var isProcessing by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { if (!isProcessing) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(20.dp))
                .testTag("payment_gateway_dialog"),
            color = HoneySurfaceCard,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Shaparak / Bank Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(HoneyGreenAccent.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "امنیت شاپرک",
                                tint = HoneyGreenAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "درگاه پرداخت اینترنتی شاپرک",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = HoneyDarkBrown
                            )
                            Text(
                                text = "پذیرنده: فروشگاه مرکزی عسل ساوالان",
                                fontSize = 11.sp,
                                color = HoneyTextSecondary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "تراکنش امن SSL",
                            color = Color(0xFF1D4ED8),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = HoneyBorder)

                // Payable Amount Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HoneySurfaceVariant, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "مبلغ قابل پرداخت:",
                            fontSize = 13.sp,
                            color = HoneyDarkBrown
                        )
                        Text(
                            text = PersianUtils.formatPrice(payableAmountTomans),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = HoneyGoldPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Card Number Field
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { cardNumber = it },
                    label = { Text("شماره کارت بانکی", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, tint = HoneyGoldPrimary)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("payment_card_input"),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HoneyGoldPrimary,
                        unfocusedBorderColor = HoneyBorder
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // CVV2 and Expiration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = cvv2,
                        onValueChange = { cvv2 = it },
                        label = { Text("CVV2", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f).testTag("payment_cvv2_input"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HoneyGoldPrimary,
                            unfocusedBorderColor = HoneyBorder
                        )
                    )

                    OutlinedTextField(
                        value = expireMonth,
                        onValueChange = { expireMonth = it },
                        label = { Text("ماه", fontSize = 11.sp) },
                        modifier = Modifier.weight(0.8f),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HoneyGoldPrimary,
                            unfocusedBorderColor = HoneyBorder
                        )
                    )

                    OutlinedTextField(
                        value = expireYear,
                        onValueChange = { expireYear = it },
                        label = { Text("سال", fontSize = 11.sp) },
                        modifier = Modifier.weight(0.8f),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HoneyGoldPrimary,
                            unfocusedBorderColor = HoneyBorder
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Dynamic OTP
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = dynamicOtp,
                        onValueChange = { dynamicOtp = it },
                        label = { Text("رمز دوم پویا", fontSize = 11.sp) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = HoneyGoldPrimary)
                        },
                        modifier = Modifier.weight(1f).testTag("payment_otp_input"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HoneyGoldPrimary,
                            unfocusedBorderColor = HoneyBorder
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { dynamicOtp = (10000 + (Math.random() * 90000).toInt()).toString() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HoneySurfaceVariant,
                            contentColor = HoneyDarkBrown
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(52.dp)
                    ) {
                        Text(text = "دریافت رمز", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (isProcessing) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = HoneyGoldPrimary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "در حال اتصال به بانک و ثبت تراکنش...",
                            fontSize = 12.sp,
                            color = HoneyDarkBrown
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Successful payment simulation
                        Button(
                            onClick = {
                                isProcessing = true
                                onPaymentResult(true)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HoneyGreenAccent,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("btn_payment_success")
                        ) {
                            Text(text = "پرداخت موفق", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        // Failed simulation
                        OutlinedButton(
                            onClick = { onPaymentResult(false) },
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, HoneyRedAccent),
                            modifier = Modifier.weight(0.7f).testTag("btn_payment_cancel")
                        ) {
                            Text(text = "انصراف", color = HoneyRedAccent, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
