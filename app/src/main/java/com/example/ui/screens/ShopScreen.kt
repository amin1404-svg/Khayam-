package com.example.ui.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.components.ProductCard
import com.example.ui.theme.HoneyBorder
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyGoldSecondary
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneySurfaceVariant
import com.example.ui.theme.HoneyTextSecondary
import com.example.util.PersianUtils

@Composable
fun ShopScreen(
    products: List<Product>,
    searchQuery: String,
    selectedCategory: String,
    selectedSort: String,
    selectedWeight: Int?,
    onSearchChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onSortChange: (String) -> Unit,
    onWeightChange: (Int?) -> Unit,
    onProductClick: (Long) -> Unit,
    onAddToCart: (Product) -> Unit,
    onToggleFavorite: (Product) -> Unit,
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

    val sortOptions = listOf("محبوب‌ترین", "جدیدترین", "ارزان‌ترین", "گران‌ترین")
    val weightOptions = listOf(250, 500, 1000, 2000)

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("shop_screen_root")
    ) {
        // Search Input Field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("shop_search_field"),
                placeholder = {
                    Text(
                        text = "جستجوی عسل طبیعی، گون، آویشن...",
                        fontSize = 13.sp,
                        color = HoneyTextSecondary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "جستجو",
                        tint = HoneyGoldPrimary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "پاک کردن",
                                tint = HoneyTextSecondary
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HoneyGoldPrimary,
                    unfocusedBorderColor = HoneyBorder,
                    focusedContainerColor = HoneySurfaceCard,
                    unfocusedContainerColor = HoneySurfaceCard
                )
            )
        }

        // Category Filter Chips (Horizontal Scroll)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategoryChange(category) },
                    label = {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = HoneyGoldPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = HoneySurfaceCard,
                        labelColor = HoneyDarkBrown
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) HoneyGoldPrimary else HoneyBorder,
                        selectedBorderColor = HoneyGoldPrimary,
                        enabled = true,
                        selected = isSelected
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("shop_cat_$category")
                )
            }
        }

        // Sort and Weight Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "فیلترها",
                tint = HoneyDarkBrown,
                modifier = Modifier.size(18.dp)
            )

            // Sort Pills
            sortOptions.forEach { sort ->
                val isSelected = selectedSort == sort
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) HoneyDarkBrown else HoneySurfaceVariant)
                        .clickable { onSortChange(sort) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = sort,
                        fontSize = 11.sp,
                        color = if (isSelected) Color.White else HoneyDarkBrown,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(18.dp)
                    .background(HoneyBorder)
            )
            Spacer(modifier = Modifier.width(4.dp))

            // Weight Filters
            weightOptions.forEach { weight ->
                val isSelected = selectedWeight == weight
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) HoneyGoldSecondary else HoneySurfaceVariant)
                        .clickable { onWeightChange(weight) }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = PersianUtils.formatWeight(weight),
                        fontSize = 11.sp,
                        color = if (isSelected) HoneyDarkBrown else HoneyDarkBrown,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Product Count summary
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${PersianUtils.toPersianDigits(products.size.toString())} محصول یافته شد",
                style = MaterialTheme.typography.bodySmall,
                color = HoneyTextSecondary,
                fontSize = 12.sp
            )

            if (selectedCategory != "همه" || searchQuery.isNotEmpty() || selectedWeight != null) {
                Text(
                    text = "پاکسازی فیلترها",
                    style = MaterialTheme.typography.labelSmall,
                    color = HoneyGoldPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onCategoryChange("همه")
                        onSearchChange("")
                        onWeightChange(null)
                    }
                )
            }
        }

        // Product Grid
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "محصولی با این مشخصات یافت نشد",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "لطفاً عبارت جستجو یا فیلترهای انتخابی را تغییر دهید.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = HoneyTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = { onProductClick(product.id) },
                        onAddToCart = { onAddToCart(product) },
                        onToggleFavorite = { onToggleFavorite(product) }
                    )
                }
            }
        }
    }
}
