package com.example.ui.components

import com.example.R

object DrawableResolver {
    fun resolve(name: String): Int {
        return when (name) {
            "img_app_icon" -> R.drawable.img_app_icon
            "img_hero_banner" -> R.drawable.img_hero_banner
            "img_honey_thyme" -> R.drawable.img_honey_thyme
            "img_honey_gavan" -> R.drawable.img_honey_gavan
            else -> R.drawable.img_honey_natural
        }
    }
}
