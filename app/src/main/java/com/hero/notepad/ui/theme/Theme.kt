package com.hero.notepad.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = DarkGray500,
    primaryVariant = DarkGray700,
    background = DarkGray500,
    onBackground = White,
    secondary = White,
    onSurface = DarkGray700,
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = White,
    primaryVariant = White,
    background = White,
    onBackground = DarkGray700,
    secondary = DarkGray500,
    onSurface = DarkGray700,
)

@Composable
fun NotepadTheme(
    darkTheme: Boolean = true /*isSystemInDarkTheme() */,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}