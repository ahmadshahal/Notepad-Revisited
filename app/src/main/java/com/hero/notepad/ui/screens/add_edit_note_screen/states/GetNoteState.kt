package com.hero.notepad.ui.screens.add_edit_note_screen.states

import com.hero.notepad.data.local.app_database.entities.Note

data class GetNoteState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val note: Note? = null
)
