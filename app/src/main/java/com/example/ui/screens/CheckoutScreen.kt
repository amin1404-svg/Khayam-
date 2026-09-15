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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.ui.theme.HoneyBorder
import com.example.ui.theme.HoneyCreamBg
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyGreenAccent
import com.example.ui.theme.HoneyRedAccent
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneySurfaceVariant
import com.example.ui.theme.HoneyTextSecondary
import com.example.util.PersianUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartItems: List<CartItem>,
    couponDiscount: Long,
    onBack: () -> Unit,
    onSubmitCheckout: (
        customerName: String,
        customerPhone: String,
        province: String,
        city: String,
        address: String,
        postalCode: String,
        orderNotes: String,
        shippingMethod: String,
        paymentMethod: String,
        shippingCost: Long
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var fullName by remember { mutableStateOf("امین رضایی") }
    var phone by remember { mutableStateOf("۰۹۱۲۳۴۵۶۷۸۹") }
    var province by remember { mutableStateOf("تهران") }
    var city by remember { mutableStateOf("تهران") }
    var address by remember { mutableStateOf("خیابان سبلان شمالی، کوچه بهار، پلاک ۱۲، واحد ۴") }
    var postalCode by remember { mutableStateOf("۱۶۴۵۸۹۳۲۱۱") }
    var orderNotes by remember { mutableStateOf("لطفاً بسته‌بندی ضدضربه و محکم باشد.") }

    var selectedShipping by remember { mutableStateOf("ارسال مستقیم توسط زنبوردار ساوالان") }
    var selectedPayment by remember { mutableStateOf("پرداخت آنلاین (شتابی)") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val subtotal = cartItems.sumOf { it.totalPriceTomans }
    val baseShipping = if (subtotal >= 700000L) 0L else 45000L
    val extraShipping = when (selectedShipping) {
        "ارسال پیک اکسپرس" -> 25000L
        else -> 0L
    }
    val shippingCost = baseShipping + extraShipping
    val totalPayable = (subtotal + shippingCost - couponDiscount).coerceAtLeast(0L)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ثبت نهایی سفارش",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("checkout_back_btn")) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = HoneyDarkBrown
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HoneySurfaceCard
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = HoneySurfaceCard,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = HoneyRedAccent,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "مبلغ نهایی پرداخت:",
                                fontSize = 11.sp,
                                color = HoneyTextSecondary
                            )
                            Text(
                                text = PersianUtils.formatPrice(totalPayable),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = HoneyDarkBrown
                            )
                        }

                        Button(
                            onClick = {
                                if (fullName.isBlank() || phone.isBlank() || address.isBlank()) {
                                    errorMessage = "لطفاً نام، شماره تماس و آدرس دقیق را وارد فرمایید."
                                } else {
                                    errorMessage = null
                                    onSubmitCheckout(
                                        fullName,
                                        phone,
                                        province,
                                        city,
                                        address,
                                        postalCode,
                                        orderNotes,
                                        selectedShipping,
                                        selectedPayment,
                                        shippingCost
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HoneyGoldPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("btn_final_pay_order")
                        ) {
                            Text(
                                text = if (selectedPayment.contains("آنلاین")) "پرداخت و ثبت نهایی" else "ثبت نهایی سفارش",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(HoneyCreamBg)
                .testTag("checkout_scroll_view"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Customer Information Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = HoneyGoldPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "اطلاعات خریدار و تحویل‌گیرنده",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("نام و نام خانوادگی", fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("checkout_name_input"),
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
                            label = { Text("شماره همراه", fontSize = 12.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth().testTag("checkout_phone_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HoneyGoldPrimary,
                                unfocusedBorderColor = HoneyBorder
                            )
                        )
                    }
                }
            }

            // 2. Shipping Address Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = HoneyGoldPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "آدرس پستی ارسال",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = province,
                                onValueChange = { province = it },
                                label = { Text("استان", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HoneyGoldPrimary,
                                    unfocusedBorderColor = HoneyBorder
                                )
                            )

                            OutlinedTextField(
                                value = city,
                                onValueChange = { city = it },
                                label = { Text("شهر", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HoneyGoldPrimary,
                                    unfocusedBorderColor = HoneyBorder
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("آدرس دقیق پستی (خیابان، کوچه، پلاک، واحد)", fontSize = 12.sp) },
                            minLines = 2,
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth().testTag("checkout_address_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HoneyGoldPrimary,
                                unfocusedBorderColor = HoneyBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = postalCode,
                            onValueChange = { postalCode = it },
                            label = { Text("کد پستی ۱۰ رقمی", fontSize = 12.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().testTag("checkout_postal_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HoneyGoldPrimary,
                                unfocusedBorderColor = HoneyBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = orderNotes,
                            onValueChange = { orderNotes = it },
                            label = { Text("توضیحات و یادداشت سفارش (اختیاری)", fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HoneyGoldPrimary,
                                unfocusedBorderColor = HoneyBorder
                            )
                        )
                    }
                }
            }

            // 3. Shipping Methods Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = null,
                                tint = HoneyGoldPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "شیوه ارسال",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val shippingOptions = listOf(
                            "ارسال مستقیم توسط زنبوردار ساوالان" to "بسته‌بندی ایمن مخصوص عسل و ارسال سریع",
                            "پست پیشتاز" to "تحویل ۲ تا ۳ روز کاری به سراسر ایران",
                            "ارسال پیک اکسپرس" to "تحویل سریع درون‌شهری (۲۵,۰۰۰ تومان مازاد)"
                        )

                        shippingOptions.forEach { (title, desc) ->
                            val isSelected = selectedShipping == title
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) HoneySurfaceVariant else Color.Transparent)
                                    .clickable { selectedShipping = title }
                                    .padding(vertical = 8.dp, horizontal = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedShipping = title },
                                    colors = RadioButtonDefaults.colors(selectedColor = HoneyGoldPrimary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = title,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = HoneyDarkBrown,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = desc,
                                        fontSize = 11.sp,
                                        color = HoneyTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Payment Methods Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = null,
                                tint = HoneyGoldPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "روش پرداخت",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val paymentOptions = listOf(
                            "پرداخت آنلاین (شتابی)" to "اتصال آنی به درگاه اینترنتی شاپرک با کلیه کارت‌های عضو شتاب",
                            "پرداخت در محل (کارتخوان)" to "پرداخت هنگام تحویل مرسوله (برای شهرهای منتخب)"
                        )

                        paymentOptions.forEach { (title, desc) ->
                            val isSelected = selectedPayment == title
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) HoneySurfaceVariant else Color.Transparent)
                                    .clickable { selectedPayment = title }
                                    .padding(vertical = 8.dp, horizontal = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedPayment = title },
                                    colors = RadioButtonDefaults.colors(selectedColor = HoneyGoldPrimary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = title,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = HoneyDarkBrown,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = desc,
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
    }
}
