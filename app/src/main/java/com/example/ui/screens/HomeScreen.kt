package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Product
import com.example.ui.components.ProductCard
import com.example.ui.theme.HoneyBorder
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyGoldSecondary
import com.example.ui.theme.HoneyGreenAccent
import com.example.ui.theme.HoneyGreenLight
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneySurfaceVariant
import com.example.ui.theme.HoneyTextPrimary
import com.example.ui.theme.HoneyTextSecondary

@Composable
fun HomeScreen(
    products: List<Product>,
    onProductClick: (Long) -> Unit,
    onAddToCart: (Product) -> Unit,
    onToggleFavorite: (Product) -> Unit,
    onNavigateToShop: (String) -> Unit,
    onNavigateToContact: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "همه",
        "عسل طبیعی",
        "عسل گون",
        "عسل آویشن",
        "عسل چندگیاه",
        "محصولات ویژه"
    )

    val bestSellers = products.filter { it.isBestSeller }
    val specialOffers = products.filter { it.isSpecialOffer }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_scroll"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Hero Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .testTag("hero_banner"),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner),
                        contentDescription = "عسل طبیعی ساوالان",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient overlay for readable text
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(HoneyGoldPrimary, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "مستقیم از تولیدکننده",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "عسل طبیعی ساوالان",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )

                        Text(
                            text = "تضمین ۱۰۰٪ خلوص و ساکارز زیر ۲ درصد آزمایشگاهی",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFFDE68A),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { onNavigateToShop("همه") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HoneyGoldSecondary,
                                contentColor = HoneyDarkBrown
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("btn_view_products")
                        ) {
                            Text(
                                text = "مشاهده محصولات",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // 2. Categories Scroll
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Text(
                    text = "دسته‌بندی‌های عسل ساوالان",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = HoneyDarkBrown,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(HoneySurfaceCard)
                                .border(1.dp, HoneyGoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .clickable { onNavigateToShop(cat) }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .testTag("category_chip_$cat")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(HoneyGoldPrimary, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = cat,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = HoneyDarkBrown
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Best Sellers Section (پرفروش‌ترین‌ها)
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "پرفروش‌ترین‌ها",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown
                    )

                    Text(
                        text = "مشاهده همه",
                        style = MaterialTheme.typography.labelMedium,
                        color = HoneyGoldPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToShop("همه") }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(bestSellers) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product.id) },
                            onAddToCart = { onAddToCart(product) },
                            onToggleFavorite = { onToggleFavorite(product) },
                            modifier = Modifier.width(200.dp)
                        )
                    }
                }
            }
        }

        // 4. Special Offers Section (پیشنهاد ویژه)
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "پیشنهاد ویژه با تخفیف طلایی",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown
                    )

                    Text(
                        text = "مشاهده همه",
                        style = MaterialTheme.typography.labelMedium,
                        color = HoneyGoldPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToShop("همه") }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(specialOffers) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product.id) },
                            onAddToCart = { onAddToCart(product) },
                            onToggleFavorite = { onToggleFavorite(product) },
                            modifier = Modifier.width(200.dp)
                        )
                    }
                }
            }
        }

        // 5. Why Savalan Honey? (چرا عسل ساوالان؟)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = HoneySurfaceVariant),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, HoneyBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "چرا عسل ساوالان؟",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown
                    )
                    Text(
                        text = "کیفیت بی‌همتا حاصل زنبورداری اصیل در مرتفع‌ترین مراتع ایران",
                        style = MaterialTheme.typography.bodySmall,
                        color = HoneyTextSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    val features = listOf(
                        FeatureItem("تولید مستقیم", "بدون واسطه و دلال، مستقیماً از دست زنبوردار", Icons.Filled.Spa),
                        FeatureItem("کیفیت تضمین‌شده", "دارای برگه آنالیز رسمی آزمایشگاهی و ساکارز زیر ۲٪", Icons.Filled.Verified),
                        FeatureItem("بسته‌بندی بهداشتی", "ظروف شیشه‌ای استاندارد با پلمپ ایمن و پاکیزه", Icons.Filled.Security),
                        FeatureItem("ارسال به سراسر کشور", "ارسال سریع و مطمئن با بسته‌بندی ضدضربه به تمام ایران", Icons.Filled.LocalShipping)
                    )

                    features.forEach { feature ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(HoneyGreenLight, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = feature.icon,
                                    contentDescription = feature.title,
                                    tint = HoneyGreenAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "✓ ${feature.title}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = HoneyDarkBrown
                                )
                                Text(
                                    text = feature.desc,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = HoneyTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 6. Producer Introduction Section (بخش معرفی تولیدکننده)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("producer_card"),
                colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, HoneyGoldPrimary.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, HoneyGoldPrimary, RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon),
                            contentDescription = "زنبوردار ساوالان",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "خرید مستقیم از تولیدکننده",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HoneyDarkBrown
                        )

                        Text(
                            text = "با حفظ کیفیت و اصالت عسل و مشاوره تخصصی درمانی",
                            style = MaterialTheme.typography.bodySmall,
                            color = HoneyTextSecondary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                        )

                        Button(
                            onClick = onNavigateToContact,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HoneyGoldPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_contact_producer_home")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PhoneInTalk,
                                contentDescription = "تماس",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ارتباط با تولیدکننده",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

data class FeatureItem(
    val title: String,
    val desc: String,
    val icon: ImageVector
)
