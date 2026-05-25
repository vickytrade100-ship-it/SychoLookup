package com.sycho.lookup.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary          = CyberBlue,
    onPrimary        = DeepNavy,
    primaryContainer = CyberBlueDark,
    secondary        = NeonGreen,
    onSecondary      = DeepNavy,
    background       = DeepNavy,
    onBackground     = OnSurfaceDark,
    surface          = SurfaceDark,
    onSurface        = OnSurfaceDark,
    surfaceVariant   = CardDark,
    onSurfaceVariant = SubtextDark,
    error            = ErrorRed,
    outline          = DividerDark
)

private val LightColorScheme = lightColorScheme(
    primary          = LightPrimary,
    onPrimary        = LightSurface,
    background       = LightBackground,
    onBackground     = Color(0xFF0A1929),
    surface          = LightSurface,
    onSurface        = Color(0xFF0A1929),
    surfaceVariant   = LightCard,
    onSurfaceVariant = Color(0xFF4A6080)
)

@Composable
fun SychoLookupTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(colorScheme = colorScheme, typography = SychoTypography, content = content)
}
