package com.hero.notepad.common

sealed class UiEvent {
    data class ShowSnackBar(val message: String, val action: String) : UiEvent()
}
