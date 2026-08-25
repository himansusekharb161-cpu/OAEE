package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AoeeLightColorScheme = lightColorScheme(
    primary = ElectricCyan,
    onPrimary = Color.White,
    primaryContainer = DeepIndigo,
    onPrimaryContainer = CyanGlow,
    secondary = GoldAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),
    background = AoeeNavyBg,
    onBackground = TextWhitePrimary,
    surface = AoeeCardBg,
    onSurface = TextWhitePrimary,
    surfaceVariant = Color(0xFFF3EDF7),
    onSurfaceVariant = TextMutedSecondary,
    outline = AoeeCardBorder,
    error = PoliceRedAlert,
    onError = Color.White,
    errorContainer = PoliceAlertBg,
    onErrorContainer = PoliceRedAlert
)

@Composable
fun AoeeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AoeeLightColorScheme,
        typography = Typography,
        content = content
    )
}
