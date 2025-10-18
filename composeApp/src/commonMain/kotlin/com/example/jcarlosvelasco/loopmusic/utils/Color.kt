package com.example.jcarlosvelasco.loopmusic.utils

import androidx.compose.ui.graphics.Color

fun Color.luminance(): Double {
    val r = (red * 255).toInt()
    val g = (green * 255).toInt()
    val b = (blue * 255).toInt()
    return (0.299 * r + 0.587 * g + 0.114 * b) / 255.0
}

fun needsContrast(color: Color, isDarkTheme: Boolean): Boolean {
    val lum = color.luminance()
    return if (isDarkTheme) lum < 0.2
    else lum > 0.8
}
