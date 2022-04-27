package com.hero.notepad.ui.screens.notes_list_screen.ui_events

sealed class UiEvent {
    data class ShowSnackBar(val message: String, val action: String) : UiEvent()
}
