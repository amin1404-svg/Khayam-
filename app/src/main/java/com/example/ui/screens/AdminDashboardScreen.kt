package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ContactMessage
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.Product
import com.example.ui.theme.HoneyBorder
import com.example.ui.theme.HoneyCreamBg
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyGoldSecondary
import com.example.ui.theme.HoneyGreenAccent
import com.example.ui.theme.HoneyGreenLight
import com.example.ui.theme.HoneyRedAccent
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneySurfaceVariant
import com.example.ui.theme.HoneyTextSecondary
import com.example.util.PersianUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    orders: List<Order>,
    products: List<Product>,
    contactMessages: List<ContactMessage>,
    onUpdateOrderStatus: (Long, OrderStatus) -> Unit,
    onSaveProduct: (Product) -> Unit,
    onDeleteProduct: (Product) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Dashboard, 1: Orders, 2: Products, 3: Messages
    var productToEdit by remember { mutableStateOf<Product?>(null) }
    var showAddEditDialog by remember { mutableStateOf(false) }

    val totalSales = orders.filter { it.status != OrderStatus.CANCELLED }.sumOf { it.totalPayableTomans }
    val pendingOrders = orders.filter { it.status == OrderStatus.SUBMITTED || it.status == OrderStatus.PREPARING }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "داشبورد مدیریت زنبوردار ساوالان",
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(HoneyCreamBg)
                .testTag("admin_dashboard_root")
        ) {
            // Tabs Row
            val tabTitles = listOf("آمار و شاخص‌ها", "سفارش‌ها (${orders.size})", "محصولات (${products.size})", "پیام‌ها (${contactMessages.size})")

            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = HoneySurfaceCard,
                contentColor = HoneyGoldPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = HoneyGoldPrimary,
                        height = 3.dp
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp,
                                color = if (selectedTab == index) HoneyDarkBrown else HoneyTextSecondary
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> AdminOverviewTab(orders.size, totalSales, pendingOrders.size, products.size)
                1 -> AdminOrdersTab(orders, onUpdateOrderStatus)
                2 -> AdminProductsTab(
                    products = products,
                    onAddNew = {
                        productToEdit = null
                        showAddEditDialog = true
                    },
                    onEdit = {
                        productToEdit = it
                        showAddEditDialog = true
                    },
                    onDelete = onDeleteProduct
                )
                3 -> AdminMessagesTab(contactMessages)
            }
        }
    }

    if (showAddEditDialog) {
        ProductEditDialog(
            initialProduct = productToEdit,
            onDismiss = { showAddEditDialog = false },
            onSave = {
                onSaveProduct(it)
                showAddEditDialog = false
            }
        )
    }
}

@Composable
fun AdminOverviewTab(
    totalOrdersCount: Int,
    totalSalesTomans: Long,
    pendingOrdersCount: Int,
    activeProductsCount: Int
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "خلاصه عملکرد زنبورستان ساوالان",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HoneyDarkBrown
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KpiCard(
                    title = "کل فروش",
                    value = PersianUtils.formatPrice(totalSalesTomans),
                    icon = Icons.Default.TrendingUp,
                    tintColor = HoneyGreenAccent,
                    modifier = Modifier.weight(1f)
                )

                KpiCard(
                    title = "تعداد سفارشات",
                    value = "${PersianUtils.toPersianDigits(totalOrdersCount.toString())} عدد",
                    icon = Icons.Default.ReceiptLong,
                    tintColor = HoneyGoldPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KpiCard(
                    title = "سفارشات در انتظار آماده‌سازی",
                    value = "${PersianUtils.toPersianDigits(pendingOrdersCount.toString())} مورد",
                    icon = Icons.Default.Payments,
                    tintColor = HoneyRedAccent,
                    modifier = Modifier.weight(1f)
                )

                KpiCard(
                    title = "انواع عسل در انبار",
                    value = "${PersianUtils.toPersianDigits(activeProductsCount.toString())} مدل",
                    icon = Icons.Default.Inventory,
                    tintColor = HoneyDarkBrown,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "راهنمای مدیریت تولیدکننده",
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "۱. با تغییر مرحله وضعیت هر سفارش، وضعیت آن به صورت زنده در گوشی خریدار به‌روز می‌شود.\n۲. قیمت‌ها و موجودی عسل‌ها را متناسب با فصل برداشت به‌روزرسانی فرمایید.\n۳. پیام‌های مشاوره خریداران را از تب پیام‌ها مطالعه و پاسخ دهید.",
                        fontSize = 12.sp,
                        color = HoneyTextSecondary,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    icon: ImageVector,
    tintColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(tintColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = tintColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, fontSize = 11.sp, color = HoneyTextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = HoneyDarkBrown)
        }
    }
}

@Composable
fun AdminOrdersTab(
    orders: List<Order>,
    onUpdateOrderStatus: (Long, OrderStatus) -> Unit
) {
    if (orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Text(text = "سفارشی برای مدیریت وجود ندارد", color = HoneyDarkBrown, fontWeight = FontWeight.Bold)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders, key = { it.id }) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                                color = HoneyDarkBrown
                            )
                            Text(
                                text = order.status.titleFa,
                                fontWeight = FontWeight.Bold,
                                color = HoneyGoldPrimary,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "مشتری: ${order.customerName} (${order.customerPhone})",
                            fontSize = 12.sp,
                            color = HoneyDarkBrown
                        )

                        Text(
                            text = "آدرس: ${order.province}، ${order.city}، ${order.address}",
                            fontSize = 11.sp,
                            color = HoneyTextSecondary,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "اقلام: ${order.itemsSummary}",
                            fontSize = 11.sp,
                            color = HoneyDarkBrown
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "مبلغ: ${PersianUtils.formatPrice(order.totalPayableTomans)} (${order.paymentMethod})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = HoneyGoldPrimary
                        )

                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = HoneyBorder)

                        Text(
                            text = "تغییر وضعیت مرسوله توسط زنبوردار:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = HoneyDarkBrown
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Status transition chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OrderStatus.values().forEach { st ->
                                val isSelected = order.status == st
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) HoneyGoldPrimary else HoneySurfaceVariant)
                                        .clickable { onUpdateOrderStatus(order.id, st) }
                                        .padding(horizontal = 8.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = st.titleFa,
                                        fontSize = 10.sp,
                                        color = if (isSelected) Color.White else HoneyDarkBrown,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
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

@Composable
fun AdminProductsTab(
    products: List<Product>,
    onAddNew: () -> Unit,
    onEdit: (Product) -> Unit,
    onDelete: (Product) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Button(
                onClick = onAddNew,
                colors = ButtonDefaults.buttonColors(
                    containerColor = HoneyGoldPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().testTag("btn_admin_add_product")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "افزودن محصول جدید به زنبورستان", fontWeight = FontWeight.Bold)
            }
        }

        items(products, key = { it.id }) { product ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = HoneyDarkBrown)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "قیمت پایه: ${PersianUtils.formatPrice(product.basePriceTomans)} | موجودی: ${PersianUtils.toPersianDigits(product.stockQuantity.toString())} عدد",
                            fontSize = 11.sp,
                            color = HoneyTextSecondary
                        )
                        if (product.discountPercent > 0) {
                            Text(
                                text = "تخفیف: ${PersianUtils.formatDiscount(product.discountPercent)}",
                                fontSize = 10.sp,
                                color = HoneyRedAccent,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row {
                        IconButton(onClick = { onEdit(product) }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "ویرایش", tint = HoneyGoldPrimary)
                        }
                        IconButton(onClick = { onDelete(product) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = HoneyRedAccent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMessagesTab(messages: List<ContactMessage>) {
    if (messages.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Text(text = "پیامی از سمت مشتریان ارسال نشده است", color = HoneyDarkBrown)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = msg.senderName, fontWeight = FontWeight.Bold, color = HoneyDarkBrown, fontSize = 13.sp)
                            Text(text = msg.dateText, fontSize = 10.sp, color = HoneyTextSecondary)
                        }
                        Text(text = "تماس: ${msg.phoneNumber}", fontSize = 11.sp, color = HoneyGoldPrimary)
                        Text(text = "موضوع: ${msg.subject}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = HoneyDarkBrown)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = msg.messageText, fontSize = 12.sp, color = HoneyDarkBrown, lineHeight = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductEditDialog(
    initialProduct: Product?,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var title by remember { mutableStateOf(initialProduct?.title ?: "") }
    var subtitle by remember { mutableStateOf(initialProduct?.subtitle ?: "") }
    var type by remember { mutableStateOf(initialProduct?.honeyType ?: "عسل طبیعی") }
    var priceText by remember { mutableStateOf(initialProduct?.basePriceTomans?.toString() ?: "350000") }
    var stockText by remember { mutableStateOf(initialProduct?.stockQuantity?.toString() ?: "50") }
    var discountText by remember { mutableStateOf(initialProduct?.discountPercent?.toString() ?: "0") }
    var description by remember { mutableStateOf(initialProduct?.description ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
            shape = RoundedCornerShape(18.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = if (initialProduct == null) "افزودن عسل جدید" else "ویرایش محصول عسل",
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown
                    )
                }

                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("نام محصول", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = subtitle,
                        onValueChange = { subtitle = it },
                        label = { Text("توضیح کوتاه", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = type,
                        onValueChange = { type = it },
                        label = { Text("نوع عسل (طبیعی، گون، آویشن، چندگیاه)", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("قیمت پایه ۵۰۰ گرمی (تومان)", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { stockText = it },
                        label = { Text("موجودی در انبار", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = discountText,
                        onValueChange = { discountText = it },
                        label = { Text("درصد تخفیف", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("توضیحات کامل و خواص", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val basePrice = priceText.toLongOrNull() ?: 300000L
                                val stock = stockText.toIntOrNull() ?: 30
                                val discount = discountText.toIntOrNull() ?: 0

                                val product = initialProduct?.copy(
                                    title = title,
                                    subtitle = subtitle,
                                    honeyType = type,
                                    basePriceTomans = basePrice,
                                    stockQuantity = stock,
                                    discountPercent = discount,
                                    description = description
                                ) ?: Product(
                                    title = title,
                                    subtitle = subtitle,
                                    honeyType = type,
                                    basePriceTomans = basePrice,
                                    stockQuantity = stock,
                                    discountPercent = discount,
                                    description = description
                                )
                                onSave(product)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HoneyGoldPrimary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("ذخیره", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("انصراف")
                        }
                    }
                }
            }
        }
    }
}
