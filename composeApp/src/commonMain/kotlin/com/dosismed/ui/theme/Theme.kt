package com.dosismed.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta azul, pensada para una app (buen contraste y agradable a la vista).
private val Blue = Color(0xFF1E66E0)
private val BlueDark = Color(0xFF0B3C91)
private val BlueLight = Color(0xFF5B91F5)
private val Accent = Color(0xFF00B8D4)

private val LightColors = lightColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD7E3FF),
    onPrimaryContainer = Color(0xFF001A41),
    secondary = Color(0xFF3F5C9A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD9E2FF),
    onSecondaryContainer = Color(0xFF001847),
    tertiary = Accent,
    onTertiary = Color.White,
    background = Color(0xFFF4F7FD),
    onBackground = Color(0xFF1A1C1E),
    surface = Color.White,
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFE1E7F2),
    onSurfaceVariant = Color(0xFF44474F),
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFAAC7FF),
    onPrimary = Color(0xFF002E69),
    primaryContainer = Color(0xFF234578),
    onPrimaryContainer = Color(0xFFD7E3FF),
    secondary = Color(0xFFBCC6DC),
    tertiary = Color(0xFF4DD0E1),
    background = Color(0xFF0E1420),
    onBackground = Color(0xFFE3E2E6),
    surface = Color(0xFF161D2B),
    onSurface = Color(0xFFE3E2E6),
    surfaceVariant = Color(0xFF44474F),
    onSurfaceVariant = Color(0xFFC4C6CF),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
)

@Composable
fun DosisMedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
