package com.hero.notepad.ui.screens.notes_list_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hero.notepad.common.Constants
import com.hero.notepad.data.local.app_database.entities.Note
import com.hero.notepad.ui.theme.BabyBlue

@Composable
fun NoteItem(note: Note, onDeleteClicked: () -> Unit, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(9.dp))
            .background(Constants.colors[note.color])
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(end = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = note.title,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h6,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = note.description,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.body2,
            )
        }
        IconButton(
            onClick = { onDeleteClicked() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                tint = MaterialTheme.colors.onSurface,
                contentDescription = "",
                modifier = Modifier.size(22.dp)
            )
        }
    }
}