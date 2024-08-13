package org.psyche.assistant.Helper

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

class CustomTheme {
    companion object {
        fun psycheColors(
            primary: Color = Color(0xFF006400),
            onPrimary: Color = Color.White,
            secondary: Color = Color(0xFF444444),
            onSecondary: Color = Color(0xFF00FF00),
            background: Color = Color(0xFF121212),
            onBackground: Color = Color(0xFFB2B2B2),
            surface: Color = Color(0xFF1C1C1C),
            onSurface: Color = Color(0xFF00FF00),
            error: Color = Color(0xFFB00020),
            onError: Color = Color.White,
        ): Colors {
            return lightColors(
                primary = primary,
                onPrimary = onPrimary,
                secondary = secondary,
                onSecondary = onSecondary,
                background = background,
                onBackground = onBackground,
                surface = surface,
                onSurface = onSurface,
                error = error,
                onError = onError,
            )
        }
    }
}
