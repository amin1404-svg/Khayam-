package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.HoneyBorder
import com.example.ui.theme.HoneyCreamBg
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyGreenAccent
import com.example.ui.theme.HoneyGreenLight
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneySurfaceVariant
import com.example.ui.theme.HoneyTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactProducerScreen(
    onBack: () -> Unit,
    onSendMessage: (name: String, phone: String, subject: String, message: String, onSent: () -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("مشاوره انتخاب عسل درمانی") }
    var messageText by remember { mutableStateOf("") }
    var isSentSuccessfully by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ارتباط مستقیم با تولیدکننده",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "بازگشت", tint = HoneyDarkBrown)
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
                .testTag("contact_producer_scroll"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Producer Brand Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(2.dp, HoneyGoldPrimary, RoundedCornerShape(14.dp))
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_app_icon),
                                    contentDescription = "زنبوردار ساوالان",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "زنبورستان کوهستان ساوالان",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = HoneyDarkBrown
                                )
                                Text(
                                    text = "حاج اصغر مرادی و پسران (بیش از ۳۵ سال سابقه زنبورداری سنتی)",
                                    fontSize = 11.sp,
                                    color = HoneyGoldPrimary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = HoneyBorder)

                        Text(
                            text = "«ما کیفیت عسل را اعتبار خانوادگی خود می‌دانیم. تمام عسل‌های ارائه‌شده بدون حرارت‌دیدگی و از دامنه‌های سبلان، با ضمانت بی قیدوشرط آزمایشگاهی عرضه می‌گردد.»",
                            style = MaterialTheme.typography.bodySmall,
                            color = HoneyDarkBrown,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Call Action Button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(HoneySurfaceVariant)
                                .clickable {
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:09141234567")
                                    }
                                    context.startActivity(intent)
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(HoneyGreenLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = "تماس تلفنی",
                                        tint = HoneyGreenAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = "تماس مستقیم با زنبوردار", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = HoneyDarkBrown)
                                    Text(text = "۰۹۱۴-۱۲۳-۴۵۶۷ (پاسخگویی ۹ الی ۲۱)", fontSize = 11.sp, color = HoneyTextSecondary)
                                }
                            }

                            Icon(imageVector = Icons.Default.SupportAgent, contentDescription = null, tint = HoneyGoldPrimary)
                        }
                    }
                }
            }

            // Message Submission Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = HoneyGoldPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ارسال پیام و ثبت سوال درباره عسل",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (isSentSuccessfully) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(HoneyGreenLight, RoundedCornerShape(12.dp))
                                    .padding(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = HoneyGreenAccent)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "پیام شما با موفقیت به تولیدکننده ارسال شد.",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = HoneyGreenAccent
                                        )
                                        Text(
                                            text = "زنبوردار در اسرع وقت از طریق پیامک یا تماس با شما پاسخ خواهد داد.",
                                            fontSize = 11.sp,
                                            color = HoneyDarkBrown
                                        )
                                    }
                                }
                            }
                        } else {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("نام و نام خانوادگی", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HoneyGoldPrimary,
                                    unfocusedBorderColor = HoneyBorder
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("شماره همراه جهت پاسخ", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HoneyGoldPrimary,
                                    unfocusedBorderColor = HoneyBorder
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = subject,
                                onValueChange = { subject = it },
                                label = { Text("موضوع پیام", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HoneyGoldPrimary,
                                    unfocusedBorderColor = HoneyBorder
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = messageText,
                                onValueChange = { messageText = it },
                                label = { Text("متن سوال، پیشنهاد یا درخواست مشاوره شما", fontSize = 12.sp) },
                                minLines = 3,
                                maxLines = 5,
                                modifier = Modifier.fillMaxWidth().testTag("contact_message_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HoneyGoldPrimary,
                                    unfocusedBorderColor = HoneyBorder
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (messageText.isNotBlank()) {
                                        onSendMessage(
                                            if (name.isBlank()) "مشتری گرامی" else name,
                                            if (phone.isBlank()) "۰۹۱۲۰۰۰۰۰۰۰" else phone,
                                            subject,
                                            messageText
                                        ) {
                                            isSentSuccessfully = true
                                        }
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
                                    .testTag("btn_send_producer_msg")
                            ) {
                                Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "ارسال پیام به زنبوردار", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            // About Mount Sabalan and Apiaries
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Spa, contentDescription = null, tint = HoneyGreenAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "درباره زنبورستان و کوهستان سبلان",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "کوه سبلان (ساوالان) به عنوان سومین قله مرتفع ایران و دومین آتشفشان خاموش، با دریاچه‌ای زیبا در دهانه خود و صدها دشت ییلاقی گلگون، غنی‌ترین ذخیره‌گاه گیاهان دارویی جهان از جمله گون کوهی، آویشن وحشی، پونه، کاسنی و بومادران است. زنبورهای عسل در این ارتفاعات تمیزترین شهدها را با خلوص صددرصد فرآوری می‌نمایند.",
                            style = MaterialTheme.typography.bodySmall,
                            color = HoneyDarkBrown,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = HoneyGoldPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "محل استقرار کندوها: ییلاقات مشگین‌شهر و سرعین، اردبیل",
                                fontSize = 11.sp,
                                color = HoneyTextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}
