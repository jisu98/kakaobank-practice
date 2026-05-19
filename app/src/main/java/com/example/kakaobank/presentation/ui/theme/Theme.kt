package com.example.kakaobank.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorScheme = lightColorScheme(
    primary = KakaoYellow,
    onPrimary = KakaoBlack,
    primaryContainer = KakaoYellowLight,
    onPrimaryContainer = KakaoBlack,
    secondary = KakaoBlack,
    onSecondary = KakaoYellow,
    secondaryContainer = KakaoYellow,
    onSecondaryContainer = KakaoBlack,
    background = Color.White,
    onBackground = KakaoBlack,
    surface = Color.White,
    onSurface = KakaoBlack,
    surfaceContainer = KakaoGrayLight,
    onSurfaceVariant = KakaoGray,
)

@Composable
fun KakaoBankTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        content = content,
    )
}
