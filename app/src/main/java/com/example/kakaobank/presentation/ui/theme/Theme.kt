package com.example.kakaobank.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
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

private val DarkColorScheme = darkColorScheme(
    primary = KakaoYellow,
    onPrimary = KakaoBlack,
    primaryContainer = KakaoDarkYellowContainer,
    onPrimaryContainer = KakaoYellow,
    secondary = KakaoYellow,
    onSecondary = KakaoBlack,
    secondaryContainer = KakaoDarkYellowContainer,
    onSecondaryContainer = KakaoYellow,
    background = KakaoDarkBackground,
    onBackground = Color.White,
    surface = KakaoDarkSurface,
    onSurface = Color.White,
    surfaceContainer = KakaoDarkSurfaceContainer,
    onSurfaceVariant = KakaoDarkOnSurfaceVariant,
)

@Composable
fun KakaoBankTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content,
    )
}
