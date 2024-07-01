package com.deymer.ibank.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.deymer.ibank.ui.colors.black40
import com.deymer.ibank.ui.colors.dark
import com.deymer.ibank.ui.colors.dark40
import com.deymer.ibank.ui.colors.dark60
import com.deymer.ibank.ui.colors.dark80
import com.deymer.ibank.ui.colors.navy
import com.deymer.ibank.ui.colors.snow
import com.deymer.ibank.ui.colors.white40
import com.deymer.ibank.ui.colors.white60
import com.deymer.ibank.ui.colors.white80

private val DarkColorScheme = darkColorScheme(
    primary = snow,
    onPrimary = dark,
    secondary = navy,
    onSecondary = snow,
    background = dark80,
    surface = dark,
    tertiary = white60,
    onTertiary = white40,
    tertiaryContainer = white80,
    onTertiaryContainer = white60,
    scrim = white40,
)

private val LightColorScheme = lightColorScheme(
    primary = snow,
    onPrimary = dark,
    secondary = snow,
    onSecondary = snow,
    background = snow,
    surface = snow,
    tertiary = dark60,
    onTertiary = dark40,
    tertiaryContainer = dark80,
    onTertiaryContainer = dark60,
    scrim = black40,
)

@Composable
fun IBankTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
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
            window.statusBarColor = if (darkTheme) {
                dark.toArgb()
            } else {
                colorScheme.primary.toArgb()
            }
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}