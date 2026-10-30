package com.finora.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Navy = Color(0xFF070B1A)
val Blue = Color(0xFF4C8DFF)
val Violet = Color(0xFF8B5CF6)
val Mint = Color(0xFF34D399)
val Coral = Color(0xFFFF6B6B)
val Amber = Color(0xFFFBBF24)

@Composable
fun FinoraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(primary = Blue, secondary = Violet, tertiary = Mint,
            background = Navy, surface = Navy, onBackground = Color.White, onSurface = Color.White),
        content = content
    )
}
