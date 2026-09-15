package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    currentPhone: String,
    lastGeneratedOtp: String?,
    authError: String?,
    onRequestOtp: (String) -> Unit,
    onVerifyOtp: (String, String) -> Unit,
    onAuthSuccess: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var phoneInput by remember { mutableStateOf(currentPhone) }
    var otpInput by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) } // 1: Enter Phone, 2: Enter OTP

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ورود و احراز هویت", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = HoneyDarkBrown) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "بازگشت", tint = HoneyDarkBrown)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HoneySurfaceCard)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(HoneyCreamBg)
                .padding(24.dp)
                .testTag("auth_screen_root"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, HoneyGoldPrimary, CircleShape)
                    .padding(6.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_app_icon),
                    contentDescription = "عسل ساوالان",
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "عسل ساوالان",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = HoneyDarkBrown
            )

            Text(
                text = "«عطر طبیعت، طعم اصالت»",
                style = MaterialTheme.typography.labelSmall,
                color = HoneyGoldPrimary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    if (step == 1) {
                        Text(
                            text = "ورود با شماره تلفن همراه",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = HoneyDarkBrown
                        )

                        Text(
                            text = "جهت ثبت و پیگیری سفارشات، شماره همراه خود را وارد کنید.",
                            fontSize = 12.sp,
                            color = HoneyTextSecondary,
                            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = phoneInput,
                            onValueChange = { phoneInput = it },
                            label = { Text("شماره همراه (مثال: ۰۹۱۲۳۴۵۶۷۸۹)", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = HoneyGoldPrimary)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth().testTag("auth_phone_field"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HoneyGoldPrimary,
                                unfocusedBorderColor = HoneyBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                if (phoneInput.length >= 10) {
                                    onRequestOtp(phoneInput)
                                    step = 2
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HoneyGoldPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("btn_request_otp")
                        ) {
                            Text(text = "دریافت کد تأیید پیامکی", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Text(
                            text = "کد تأیید پیامک‌شده را وارد نمایید",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = HoneyDarkBrown
                        )

                        Text(
                            text = "کد ۶ رقمی به شماره ${PersianUtils.toPersianDigits(phoneInput)} ارسال شد.",
                            fontSize = 12.sp,
                            color = HoneyTextSecondary,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        // Generated OTP hint for easy testing
                        if (lastGeneratedOtp != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(HoneyGreenLight, RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "کد شبیه‌سازی‌شده جهت آزمایش: ${PersianUtils.toPersianDigits(lastGeneratedOtp)} (یا ۱۲۳۴۵۶)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HoneyGreenAccent,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        OutlinedTextField(
                            value = otpInput,
                            onValueChange = { otpInput = it },
                            label = { Text("کد ۶ رقمی تأیید", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = HoneyGoldPrimary)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().testTag("auth_otp_field"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HoneyGoldPrimary,
                                unfocusedBorderColor = HoneyBorder
                            )
                        )

                        if (authError != null) {
                            Text(
                                text = authError,
                                color = HoneyRedAccent,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                onVerifyOtp(phoneInput, otpInput)
                                onAuthSuccess()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HoneyGoldPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("btn_verify_otp")
                        ) {
                            Text(text = "تأیید و ورود به حساب", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "ویرایش شماره تلفن",
                            color = HoneyGoldPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { step = 1 }
                                .padding(6.dp)
                        )
                    }
                }
            }
        }
    }
}
