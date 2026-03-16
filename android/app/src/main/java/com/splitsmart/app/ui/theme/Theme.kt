package com.splitsmart.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Blue40, onPrimary = Color.White, primaryContainer = Blue90, onPrimaryContainer = Blue10,
    secondary = Cyan40, onSecondary = Color.White, secondaryContainer = Cyan90, onSecondaryContainer = Cyan10,
    tertiary = Purple40, onTertiary = Color.White, tertiaryContainer = Purple90, onTertiaryContainer = Purple10,
    error = ErrorRed, onError = Color.White, background = Color(0xFFFDFBFF), onBackground = Color(0xFF1A1C1E),
    surface = SurfaceLight, onSurface = Color(0xFF1A1C1E), surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = Color(0xFF44474F), outline = Color(0xFF74777F), outlineVariant = Color(0xFFC4C6D0)
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue80, onPrimary = Blue20, primaryContainer = Blue30, onPrimaryContainer = Blue90,
    secondary = Cyan80, onSecondary = Cyan20, secondaryContainer = Cyan30, onSecondaryContainer = Cyan90,
    tertiary = Purple80, onTertiary = Purple20, tertiaryContainer = Purple30, onTertiaryContainer = Purple90,
    error = ErrorRedDark, onError = Color(0xFF690005), background = SurfaceDark, onBackground = Color(0xFFE2E2E6),
    surface = SurfaceDark, onSurface = Color(0xFFE2E2E6), surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = Color(0xFFC4C6D0), outline = Color(0xFF8E9099), outlineVariant = Color(0xFF44474F)
)

@Composable
fun SplitSmartTheme(darkTheme: Boolean = isSystemInDarkTheme(), dynamicColor: Boolean = true, content: @Composable () -> Unit) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(colorScheme = colorScheme, typography = SplitSmartTypography, content = content)
}
