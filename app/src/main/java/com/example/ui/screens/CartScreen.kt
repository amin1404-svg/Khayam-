package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CartItem
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

@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    appliedCoupon: String?,
    couponDiscount: Long,
    onQuantityChange: (CartItem, Int) -> Unit,
    onRemoveItem: (CartItem) -> Unit,
    onApplyCoupon: (String) -> Boolean,
    onRemoveCoupon: () -> Unit,
    onProceedToCheckout: () -> Unit,
    onExploreShop: () -> Unit,
    modifier: Modifier = Modifier
) {
    var couponInput by remember { mutableStateOf("") }
    var couponError by remember { mutableStateOf(false) }

    val subtotal = cartItems.sumOf { it.totalPriceTomans }
    // Free shipping threshold 700,000 Tomans
    val shippingCost = if (subtotal >= 700000L || cartItems.isEmpty()) 0L else 45000L
    val totalPayable = (subtotal + shippingCost - couponDiscount).coerceAtLeast(0L)

    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(HoneyCreamBg)
                .padding(32.dp)
                .testTag("empty_cart_view"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(HoneySurfaceVariant, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalMall,
                        contentDescription = "سبد خالی",
                        tint = HoneyGoldPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "سبد خرید شما در حال حاضر خالی است",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = HoneyDarkBrown
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "می‌توانید محصولات ناب عسل ساوالان را بررسی کرده و به سبد خود اضافه فرمایید.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HoneyTextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onExploreShop,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HoneyGoldPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_explore_shop")
                ) {
                    Text(
                        text = "مشاهده محصولات فروشگاه",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(HoneyCreamBg)
                .testTag("cart_screen_root")
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Free shipping notice banner
                item {
                    if (subtotal < 700000L) {
                        val remaining = 700000L - subtotal
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(HoneySurfaceVariant, RoundedCornerShape(12.dp))
                                .border(1.dp, HoneyGoldSecondary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "تنها با خرید ${PersianUtils.formatPrice(remaining)} دیگر، ارسال برای شما رایگان خواهد شد!",
                                style = MaterialTheme.typography.bodySmall,
                                color = HoneyMediumBrown,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(HoneyGreenLight, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "✓ تبریک! سفارش شما مشمول ارسال رایگان زنبوردار ساوالان شد.",
                                style = MaterialTheme.typography.bodySmall,
                                color = HoneyGreenAccent,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Cart Items List
                items(cartItems, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("cart_item_${item.id}"),
                        colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Product Image
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(HoneyCreamBg)
                            ) {
                                Image(
                                    painter = painterResource(id = DrawableResolver.resolve(item.imageDrawableName)),
                                    contentDescription = item.productTitle,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Details
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.productTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = HoneyDarkBrown,
                                    maxLines = 1
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "وزن: ${PersianUtils.formatWeight(item.selectedWeightGrams)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = HoneyGoldPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "قیمت واحد: ${PersianUtils.formatPrice(item.unitPriceTomans)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = HoneyTextSecondary,
                                    fontSize = 11.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "جمع: ${PersianUtils.formatPrice(item.totalPriceTomans)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = HoneyDarkBrown
                                )
                            }

                            // Quantity Controller & Delete
                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = { onRemoveItem(item) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "حذف",
                                        tint = HoneyRedAccent.copy(alpha = 0.8f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(HoneySurfaceVariant, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 2.dp, vertical = 2.dp)
                                ) {
                                    IconButton(
                                        onClick = { onQuantityChange(item, item.quantity - 1) },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Remove,
                                            contentDescription = "کمتر",
                                            tint = HoneyDarkBrown,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }

                                    Text(
                                        text = PersianUtils.toPersianDigits(item.quantity.toString()),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = HoneyDarkBrown,
                                        modifier = Modifier.padding(horizontal = 6.dp)
                                    )

                                    IconButton(
                                        onClick = { onQuantityChange(item, item.quantity + 1) },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Add,
                                            contentDescription = "بیشتر",
                                            tint = HoneyDarkBrown,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Coupon Code Section
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = "کد تخفیف",
                                    tint = HoneyGoldPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "کد تخفیف دارید؟ (کد تست: SAVALAN)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = HoneyDarkBrown
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            if (appliedCoupon != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(HoneyGreenLight, RoundedCornerShape(10.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "کد $appliedCoupon اعمال شد (${PersianUtils.formatPrice(couponDiscount)} تخفیف)",
                                        color = HoneyGreenAccent,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )

                                    IconButton(
                                        onClick = onRemoveCoupon,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "حذف کد تخفیف",
                                            tint = HoneyRedAccent
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = couponInput,
                                        onValueChange = {
                                            couponInput = it
                                            couponError = false
                                        },
                                        placeholder = { Text("مثال: SAVALAN", fontSize = 12.sp) },
                                        singleLine = true,
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("coupon_input"),
                                        shape = RoundedCornerShape(10.dp),
                                        isError = couponError,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = HoneyGoldPrimary,
                                            unfocusedBorderColor = HoneyBorder
                                        )
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            val valid = onApplyCoupon(couponInput)
                                            if (!valid) couponError = true
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = HoneyGoldSecondary,
                                            contentColor = HoneyDarkBrown
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.testTag("btn_apply_coupon")
                                    ) {
                                        Text(text = "اعمال", fontWeight = FontWeight.Bold)
                                    }
                                }
                                if (couponError) {
                                    Text(
                                        text = "کد تخفیف معتبر نیست. کد تست SAVALAN را امتحان کنید.",
                                        color = HoneyRedAccent,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Invoice Summary Breakdown
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = HoneySurfaceCard),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "فاکتور و جزئیات پرداخت",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HoneyDarkBrown
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "جمع قیمت محصولات:", color = HoneyTextSecondary, fontSize = 13.sp)
                                Text(
                                    text = PersianUtils.formatPrice(subtotal),
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
                                Text(text = "هزینه ارسال:", color = HoneyTextSecondary, fontSize = 13.sp)
                                Text(
                                    text = if (shippingCost == 0L) "رایگان" else PersianUtils.formatPrice(shippingCost),
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (shippingCost == 0L) HoneyGreenAccent else HoneyDarkBrown,
                                    fontSize = 13.sp
                                )
                            }

                            if (couponDiscount > 0L) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "تخفیف کوپن:", color = HoneyRedAccent, fontSize = 13.sp)
                                    Text(
                                        text = "- ${PersianUtils.formatPrice(couponDiscount)}",
                                        fontWeight = FontWeight.Bold,
                                        color = HoneyRedAccent,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = HoneyBorder)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "مبلغ نهایی پرداخت:",
                                    fontWeight = FontWeight.Bold,
                                    color = HoneyDarkBrown,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = PersianUtils.formatPrice(totalPayable),
                                    fontWeight = FontWeight.ExtraBold,
                                    color = HoneyGoldPrimary,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Continue Checkout Action
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = HoneySurfaceCard,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "مجموع سفارش:",
                            fontSize = 11.sp,
                            color = HoneyTextSecondary
                        )
                        Text(
                            text = PersianUtils.formatPrice(totalPayable),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = HoneyDarkBrown
                        )
                    }

                    Button(
                        onClick = onProceedToCheckout,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HoneyGoldPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_proceed_checkout")
                    ) {
                        Text(
                            text = "ادامه ثبت سفارش",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
