package org.psyche.assistant.Helper

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color


class CustomTheme
{
    companion object {
        fun psycheColors(
            primary: Color = Color(0xFF2222AA),
            onPrimary: Color = Color.White,
            secondary: Color = Color.Gray,
            onSecondary: Color = Color.Cyan,
            background: Color = Color(0xFF111111),
            onBackground: Color = Color(0xFFCCCCCC),
            surface: Color = Color(0xFFAAAAAA),
            onSurface: Color = Color.Cyan,
            error: Color = Color.Red,
            onError: Color = Color.Cyan,
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