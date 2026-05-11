package com.yash.iqtest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Brand palette ──────────────────────────────────────────────────────────
val SageGreen     = Color(0xFF9CAF88)
val SageGreenDark = Color(0xFF7A9166)
val SageGreenLight= Color(0xFFBDCFAD)
val Cream         = Color(0xFFFDF6EC)
val CreamDark     = Color(0xFFF2E8D6)
val EarthyBrown   = Color(0xFF6F4E37)
val EarthyBrownLight = Color(0xFF9C7A64)
val DarkSlate     = Color(0xFF333333)
val CorrectGreen  = Color(0xFF4CAF50)
val WrongRed      = Color(0xFFE53935)
val GoldAccent    = Color(0xFFD4A017)

private val IQColorScheme = lightColorScheme(
    primary          = SageGreen,
    onPrimary        = Color.White,
    primaryContainer = SageGreenLight,
    onPrimaryContainer = DarkSlate,
    secondary        = EarthyBrown,
    onSecondary      = Color.White,
    secondaryContainer = CreamDark,
    onSecondaryContainer = DarkSlate,
    background       = Cream,
    onBackground     = DarkSlate,
    surface          = Color.White,
    onSurface        = DarkSlate,
    surfaceVariant   = CreamDark,
    onSurfaceVariant = EarthyBrown,
    outline          = SageGreenDark,
    error            = WrongRed,
    onError          = Color.White,
)

@Composable
fun IQTestTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = IQColorScheme,
        typography  = IQTypography,
        content     = content
    )
}
