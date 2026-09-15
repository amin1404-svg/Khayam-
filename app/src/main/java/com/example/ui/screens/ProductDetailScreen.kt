package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Product
import com.example.ui.components.DrawableResolver
import com.example.ui.theme.HoneyBorder
import com.example.ui.theme.HoneyCreamBg
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyGoldSecondary
import com.example.ui.theme.HoneyMediumBrown
import com.example.ui.theme.HoneyGreenAccent
import com.example.ui.theme.HoneyGreenLight
import com.example.ui.theme.HoneyRedAccent
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneySurfaceVariant
import com.example.ui.theme.HoneyTextPrimary
import com.example.ui.theme.HoneyTextSecondary
import com.example.util.PersianUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    onBack: () -> Unit,
    onAddToCart: (Product, Int, Int) -> Unit,
    onQuickBuy: (Product, Int, Int) -> Unit,
    onToggleFavorite: (Product) -> Unit,
    onContactProducerAboutProduct: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedWeight by remember { mutableIntStateOf(product.defaultWeightGrams) }
    var quantity by remember { mutableIntStateOf(1) }
    var selectedImageIndex by remember { mutableIntStateOf(0) }

    // Gallery drawables list
    val galleryImages = remember(product) {
        listOf(
            product.imageDrawableName,
            "img_hero_banner",
            "img_app_icon"
        )
    }

    val availableWeights = product.getAvailableWeights()
    val originalPrice = product.getPriceForWeight(selectedWeight)
    val unitPrice = product.getDiscountedPrice(selectedWeight)
    val totalAmount = unitPrice * quantity
    val hasDiscount = product.discountPercent > 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_btn")) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = HoneyDarkBrown
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onToggleFavorite(product) }) {
                        Icon(
                            imageVector = if (product.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "علاقه‌مندی",
                            tint = if (product.isFavorite) HoneyRedAccent else HoneyDarkBrown
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HoneySurfaceCard
                )
            )
        },
        bottomBar = {
            // Fixed bottom action bar: Total price + Quick Buy + Add to Cart
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = HoneySurfaceCard,
                shadowElevation = 12.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "مبلغ قابل پرداخت:",
                            style = MaterialTheme.typography.labelSmall,
                            color = HoneyTextSecondary
                        )
                        Text(
                            text = PersianUtils.formatPrice(totalAmount),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = HoneyDarkBrown
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Quick buy
                        OutlinedButton(
                            onClick = { onQuickBuy(product, selectedWeight, quantity) },
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, HoneyGoldPrimary),
                            modifier = Modifier.testTag("btn_quick_buy")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FlashOn,
                                contentDescription = "خرید سریع",
                                tint = HoneyGoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "خرید سریع",
                                color = HoneyGoldPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        // Add to cart
                        Button(
                            onClick = { onAddToCart(product, selectedWeight, quantity) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HoneyGoldPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("btn_add_to_cart_detail")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "افزودن به سبد",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "افزودن به سبد",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
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
                .testTag("product_detail_scroll"),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // 1. Large Hero Product Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .background(Color.White)
                ) {
                    val currentImg = galleryImages.getOrElse(selectedImageIndex) { product.imageDrawableName }
                    Image(
                        painter = painterResource(id = DrawableResolver.resolve(currentImg)),
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    if (hasDiscount) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                                .background(HoneyRedAccent, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${PersianUtils.formatDiscount(product.discountPercent)} تخفیف ویژه",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // 2. Image Gallery Thumbnails
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HoneySurfaceCard)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(galleryImages.indices.toList()) { idx ->
                        val isSelected = selectedImageIndex == idx
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    width = if (isSelected) 2.5.dp else 1.dp,
                                    color = if (isSelected) HoneyGoldPrimary else HoneyBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedImageIndex = idx }
                        ) {
                            Image(
                                painter = painterResource(id = DrawableResolver.resolve(galleryImages[idx])),
                                contentDescription = "نمای گالری",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            // 3. Product Title & Info
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = product.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = product.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = HoneyTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Honey Type pill
                            Box(
                                modifier = Modifier
                                    .background(HoneySurfaceVariant, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "نوع: ${product.honeyType}",
                                    color = HoneyGoldPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            // Stock status
                            Box(
                                modifier = Modifier
                                    .background(HoneyGreenLight, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "موجود در انبار کندوها (${PersianUtils.toPersianDigits(product.stockQuantity.toString())} عدد)",
                                    color = HoneyGreenAccent,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 14.dp), color = HoneyBorder)

                        // Weight Selection
                        Text(
                            text = "انتخاب وزن محصول:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HoneyDarkBrown
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            availableWeights.forEach { w ->
                                val isSelected = selectedWeight == w
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) HoneyGoldPrimary else HoneySurfaceVariant)
                                        .border(
                                            1.dp,
                                            if (isSelected) HoneyGoldPrimary else HoneyBorder,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable { selectedWeight = w }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = PersianUtils.formatWeight(w),
                                        color = if (isSelected) Color.White else HoneyDarkBrown,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quantity Selector & Unit Price
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Quantity
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(HoneySurfaceVariant, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                IconButton(
                                    onClick = { if (quantity > 1) quantity-- },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Remove,
                                        contentDescription = "کاهش",
                                        tint = HoneyDarkBrown
                                    )
                                }

                                Text(
                                    text = PersianUtils.toPersianDigits(quantity.toString()),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = HoneyDarkBrown,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )

                                IconButton(
                                    onClick = { if (quantity < product.stockQuantity) quantity++ },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "افزایش",
                                        tint = HoneyDarkBrown
                                    )
                                }
                            }

                            // Price
                            Column(horizontalAlignment = Alignment.End) {
                                if (hasDiscount) {
                                    Text(
                                        text = PersianUtils.formatPrice(originalPrice),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            textDecoration = TextDecoration.LineThrough
                                        ),
                                        color = HoneyTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                                Text(
                                    text = PersianUtils.formatPrice(unitPrice),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = HoneyDarkBrown
                                )
                            }
                        }
                    }
                }
            }

            // 4. Product Specifications Grid (ویژگی‌های محصول)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ویژگی‌های تخصصی و آزمایشگاهی",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HoneyDarkBrown
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val specs = listOf(
                            "منطقه برداشت" to product.harvestRegion,
                            "درصد ساکارز" to "زیر ${PersianUtils.toPersianDigits(product.sucrosePercentage.toString())}٪ (استاندارد آزمایشگاهی)",
                            "درصد خلوص" to "${PersianUtils.toPersianDigits(product.purityPercentage.toString())}٪ طبیعی و خام",
                            "روش استخراج" to "سنتی و مکانیکی بدون حرارت‌دهی",
                            "نوع کندو" to "کندوهای چوبی ییلاقی در هوای پاک کوهستان"
                        )

                        specs.forEach { (label, value) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = HoneyTextSecondary
                                )
                                Text(
                                    text = value,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = HoneyDarkBrown
                                )
                            }
                            Divider(color = HoneyBorder.copy(alpha = 0.4f), thickness = 0.7.dp)
                        }
                    }
                }
            }

            // 5. About this Honey (درباره این عسل)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "درباره این عسل و خواص درمانی",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HoneyDarkBrown
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = HoneyTextPrimary,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(HoneySurfaceVariant, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "خواص: ${product.healthBenefits}",
                                style = MaterialTheme.typography.bodySmall,
                                color = HoneyMediumBrown,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // 6. Contact Producer about this Product
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { onContactProducerAboutProduct(product) },
                    colors = CardDefaults.cardColors(containerColor = HoneySurfaceVariant),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HoneyGoldPrimary.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(HoneyGoldPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = "چت با زنبوردار",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "سوالی درباره این عسل دارید؟",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = HoneyDarkBrown
                                )
                                Text(
                                    text = "ارتباط مستقیم و دریافت مشاوره تخصصی از زنبوردار",
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
