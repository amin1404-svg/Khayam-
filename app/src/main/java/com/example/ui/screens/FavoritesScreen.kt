package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.components.ProductCard
import com.example.ui.theme.HoneyCreamBg
import com.example.ui.theme.HoneyDarkBrown
import com.example.ui.theme.HoneyGoldPrimary
import com.example.ui.theme.HoneyRedAccent
import com.example.ui.theme.HoneySurfaceCard
import com.example.ui.theme.HoneySurfaceVariant
import com.example.ui.theme.HoneyTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favoriteProducts: List<Product>,
    onProductClick: (Long) -> Unit,
    onAddToCart: (Product) -> Unit,
    onToggleFavorite: (Product) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "علاقه‌مندی‌ها", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = HoneyDarkBrown) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "بازگشت", tint = HoneyDarkBrown)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HoneySurfaceCard)
            )
        }
    ) { innerPadding ->
        if (favoriteProducts.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(HoneyCreamBg)
                    .padding(32.dp)
                    .testTag("empty_favorites_view"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(HoneySurfaceVariant, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = HoneyRedAccent.copy(alpha = 0.5f),
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "لیست علاقه‌مندی‌ها خالی است",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HoneyDarkBrown
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "با کلیک روی آیکون قلب در کنار هر محصول، می‌توانید آن را برای خرید بعدی ذخیره کنید.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = HoneyTextSecondary,
                        fontSize = 12.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(HoneyCreamBg)
                    .testTag("favorites_grid")
            ) {
                items(favoriteProducts, key = { it.id }) { product ->
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
