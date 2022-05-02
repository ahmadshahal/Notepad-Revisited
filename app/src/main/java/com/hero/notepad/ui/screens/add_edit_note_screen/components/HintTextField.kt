package com.hero.notepad.ui.screens.add_edit_note_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun HintTextField(
    modifier: Modifier = Modifier,
    value: String,
    onChange: (String) -> Unit,
    hint: String,
    textStyle: TextStyle = TextStyle(),
) {
    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                style = textStyle.copy(
                    color = MaterialTheme.colors.onBackground.copy(
                        alpha = 0.5F
                    )
                )
            )
        }
        MaterialTheme(
            colors = darkColors(primary = MaterialTheme.colors.onBackground)
        ) {
            BasicTextField(
                modifier = modifier.fillMaxWidth(),
                value = value,
                onValueChange = onChange,
                textStyle = textStyle,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                cursorBrush = SolidColor(value = MaterialTheme.colors.onBackground)
            )
        }
    }
}