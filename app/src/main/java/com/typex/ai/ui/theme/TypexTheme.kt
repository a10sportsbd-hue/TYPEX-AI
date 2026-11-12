package com.typex.ai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TypexColors = darkColorScheme(
    primary = Color(0xFFB9A4FF),
    secondary = Color(0xFF7CE7D5),
    background = Color(0xFF0B0B12),
    surface = Color(0xFF191827),
    onPrimary = Color(0xFF171326),
    onBackground = Color(0xFFF4F0FF),
    onSurface = Color(0xFFF4F0FF)
)

@Composable
fun TypexTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TypexColors,
        content = content
    )
}
