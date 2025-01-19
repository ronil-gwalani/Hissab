package org.ronil.hissab.ui


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import org.ronil.hissab.App
import org.ronil.hissab.utils.AppColors



private val LightColorScheme = lightColorScheme(
    primary = AppColors.accentColor,
    secondary = AppColors.accentColor,
    tertiary = AppColors.textColor
    /* Other default colors to override
    background = RonColor(0xFFFFFBFE),
    surface = RonColor(0xFFFFFBFE),
    onPrimary = RonColor.White,
    onSecondary = RonColor.White,
    onTertiary = RonColor.White,
    onBackground = RonColor(0xFF1C1B1F),
    onSurface = RonColor(0xFF1C1B1F),
    */
)

@Composable
fun HissabAppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}