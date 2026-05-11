package com.yash.iqtest.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val IQTypography = Typography(
    displayLarge = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.Bold,
        fontSize    = 36.sp,
        lineHeight  = 44.sp,
        letterSpacing = (-0.5).sp,
        color       = DarkSlate
    ),
    headlineLarge = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.Bold,
        fontSize    = 28.sp,
        lineHeight  = 36.sp,
        color       = DarkSlate
    ),
    headlineMedium = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.SemiBold,
        fontSize    = 22.sp,
        lineHeight  = 28.sp,
        color       = DarkSlate
    ),
    titleLarge = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.SemiBold,
        fontSize    = 18.sp,
        lineHeight  = 26.sp,
        color       = DarkSlate
    ),
    bodyLarge = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.Normal,
        fontSize    = 16.sp,
        lineHeight  = 24.sp,
        color       = DarkSlate
    ),
    bodyMedium = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.Normal,
        fontSize    = 14.sp,
        lineHeight  = 20.sp,
        color       = DarkSlate
    ),
    labelLarge = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.SemiBold,
        fontSize    = 14.sp,
        lineHeight  = 20.sp,
        letterSpacing = 0.5.sp,
        color       = DarkSlate
    )
)
