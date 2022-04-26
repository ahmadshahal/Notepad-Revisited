package com.hero.notepad.ui.screens.notes_list_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hero.notepad.common.UiState
import com.hero.notepad.data.local.app_database.entities.Note
import com.hero.notepad.domain.usecases.DeleteNoteUseCase
import com.hero.notepad.domain.usecases.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _screenState: MutableState<UiState<List<Note>>> = mutableStateOf(UiState.Initial<List<Note>>())
    val screenState: State<UiState<List<Note>>>
        get() = _screenState

    init {
        getNotes()
    }

    fun getNotes() {
        viewModelScope.launch {
            getNotesUseCase.execute().collect { uiState ->
                _screenState.value = uiState
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase.execute(note).collect { uiState ->
                _screenState.value = uiState
            }
        }
    }
}