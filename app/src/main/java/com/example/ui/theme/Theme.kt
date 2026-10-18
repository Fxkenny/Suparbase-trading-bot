package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SuparbaseColorScheme = darkColorScheme(
    primary = SuparbaseNeonGreen,
    onPrimary = SuparbaseBlack,
    primaryContainer = SuparbaseGreenGlow,
    onPrimaryContainer = SuparbaseNeonGreen,
    secondary = SuparbaseGold,
    onSecondary = SuparbaseBlack,
    secondaryContainer = SuparbaseGoldGlow,
    onSecondaryContainer = SuparbaseGold,
    tertiary = SuparbaseCyan,
    onTertiary = SuparbaseBlack,
    error = SuparbaseRed,
    onError = TextWhite,
    background = SuparbaseBlack,
    onBackground = TextWhite,
    surface = SuparbaseDarkCard,
    onSurface = TextWhite,
    surfaceVariant = SuparbaseDarkCard,
    onSurfaceVariant = TextMuted,
    outline = SuparbaseCardBorderSubtle
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Enforce our branded neon Suparbase dark theme
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = SuparbaseBlack.toArgb()
            window.navigationBarColor = SuparbaseBlack.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = SuparbaseColorScheme,
        typography = Typography,
        content = content
    )
}
