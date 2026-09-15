package com.example.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import java.text.DecimalFormat

object PersianUtils {

    fun toPersianDigits(text: String): String {
        val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
        val sb = java.lang.StringBuilder()
        for (ch in text) {
            if (ch in '0'..'9') {
                sb.append(persianDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun formatPrice(amountTomans: Long): String {
        val formatter = DecimalFormat("#,###")
        val formatted = formatter.format(amountTomans)
        return "${toPersianDigits(formatted)} تومان"
    }

    fun formatDiscount(percent: Int): String {
        return "%${toPersianDigits(percent.toString())}"
    }

    fun formatWeight(weightGram: Int): String {
        return if (weightGram >= 1000) {
            val kg = weightGram / 1000.0
            if (kg == kg.toLong().toDouble()) {
                "${toPersianDigits(kg.toLong().toString())} کیلوگرم"
            } else {
                "${toPersianDigits(String.format("%.1f", kg))} کیلوگرم"
            }
        } else {
            "${toPersianDigits(weightGram.toString())} گرم"
        }
    }
}

@Composable
fun RtlProvider(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        content()
    }
}
