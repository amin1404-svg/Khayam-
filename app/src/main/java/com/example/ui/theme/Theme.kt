package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = HoneyGoldSecondary,
  onPrimary = HoneyDarkBrown,
  primaryContainer = HoneyGoldDark,
  onPrimaryContainer = HoneyCreamBg,
  secondary = HoneyGoldLight,
  onSecondary = HoneyDarkBrown,
  background = Color(0xFF1B140E),
  surface = Color(0xFF251C15),
  onBackground = Color(0xFFF7F1E5),
  onSurface = Color(0xFFF7F1E5),
  surfaceVariant = Color(0xFF382B21),
  onSurfaceVariant = Color(0xFFD6C7B2),
  outline = Color(0xFF5A4433)
)

private val LightColorScheme = lightColorScheme(
  primary = HoneyGoldPrimary,
  onPrimary = Color.White,
  primaryContainer = HoneyGoldLight,
  onPrimaryContainer = HoneyDarkBrown,
  secondary = HoneyMediumBrown,
  onSecondary = Color.White,
  tertiary = HoneyGreenAccent,
  onTertiary = Color.White,
  background = HoneyCreamBg,
  surface = HoneySurfaceCard,
  onBackground = HoneyTextPrimary,
  onSurface = HoneyTextPrimary,
  surfaceVariant = HoneySurfaceVariant,
  onSurfaceVariant = HoneyTextSecondary,
  outline = HoneyBorder,
  outlineVariant = Color(0xFFF0E5D3)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Keep consistent luxury honey identity across all devices
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

