package com.hero.notepad.ui.screens.notes_list_screen.states

import com.hero.notepad.data.local.app_database.entities.Note

data class NotesListState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val list: List<Note> = emptyList()
)
