package com.hero.notepad.ui.screens.add_edit_note_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hero.notepad.common.Constants
import com.hero.notepad.common.UiEvent
import com.hero.notepad.common.UiState
import com.hero.notepad.data.local.app_database.entities.Note
import com.hero.notepad.domain.usecases.AddEditNoteUseCase
import com.hero.notepad.domain.usecases.GetNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val addEditNoteUseCase: AddEditNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _saveNoteState = mutableStateOf<UiState<Boolean>>(UiState.Initial())
    val saveNoteState: State<UiState<Boolean>>
        get() = _saveNoteState

    private val _loadNoteState = mutableStateOf<UiState<Note>>(UiState.Initial())
    val loadNoteState: State<UiState<Note>>
        get() = _loadNoteState

    private val _uiEvent: Channel<UiEvent> = Channel()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    val titleFieldState = mutableStateOf("")
    val descriptionFieldState = mutableStateOf("")

    val dropDownMenuState = mutableStateOf(false)

    var noteColorIdx = mutableStateOf(0)

    init {
        // Can't be null because it's an optional parameter with a default value.
        val noteId: Int = savedStateHandle.get<Int>(Constants.NOTE_ID_KEY)!!
        if (noteId != -1) {
            loadNote(noteId)
        }
    }

    private fun loadNote(noteId: Int) {
        viewModelScope.launch {
            getNoteUseCase.execute(noteId).collect { uiState ->
                if (uiState is UiState.Success) {
                    titleFieldState.value = uiState.data!!.title
                    descriptionFieldState.value = uiState.data.description
                    noteColorIdx.value = uiState.data.color
                }
                _loadNoteState.value = uiState
            }
        }
    }

    fun saveNote() {
        if ((loadNoteState.value is UiState.Success
                    || loadNoteState.value is UiState.Initial)
            && titleFieldState.value.isNotEmpty()
        ) {
            viewModelScope.launch {
                addEditNoteUseCase.execute(
                    Note(
                        id = _loadNoteState.value.data?.id ?: 0,
                        title = titleFieldState.value,
                        description = descriptionFieldState.value,
                        color = noteColorIdx.value
                    ),
                ).collect { uiState ->
                    if (uiState is UiState.Success) {
                        _uiEvent.send(UiEvent.PopBackStack)
                    }
                    _saveNoteState.value = uiState
                }
            }
        }

    }
}