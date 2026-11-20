package com.example.model1gold.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ObsidianBg = Color(0xFF090B10)
val SlateSurface = Color(0xFF111827)
val SlateSurfaceVariant = Color(0xFF1E293B)
val SlateBorder = Color(0xFF334155)
val GoldPrimary = Color(0xFFF59E0B)
val GoldLight = Color(0xFFFCD34D)
val EmeraldBull = Color(0xFF10B981)
val CrimsonBear = Color(0xFFEF4444)
val SkyAccent = Color(0xFF38BDF8)
val TextPrimary = Color(0xFFF1F5F9)
val TextSecondary = Color(0xFF94A3B8)

private val Model1DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = Color(0xFF090B10),
    primaryContainer = Color(0xFF451A03),
    onPrimaryContainer = GoldLight,
    secondary = SkyAccent,
    onSecondary = Color(0xFF090B10),
    secondaryContainer = Color(0xFF0C4A6E),
    onSecondaryContainer = Color(0xFFE0F2FE),
    tertiary = EmeraldBull,
    onTertiary = Color(0xFF090B10),
    background = ObsidianBg,
    onBackground = TextPrimary,
    surface = SlateSurface,
    onSurface = TextPrimary,
    surfaceVariant = SlateSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = SlateBorder,
    error = CrimsonBear,
    onError = Color.White,
)

@Composable
fun Model1GoldTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Model1DarkColorScheme,
        content = content
    )
}
