package com.hero.notepad.ui.screens.add_edit_note_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hero.notepad.common.Result
import com.hero.notepad.data.local.app_database.entities.Note
import com.hero.notepad.domain.usecases.AddEditNoteUseCase
import com.hero.notepad.domain.usecases.GetNoteUseCase
import com.hero.notepad.ui.screens.add_edit_note_screen.states.AddEditNoteState
import com.hero.notepad.ui.screens.add_edit_note_screen.states.GetNoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val addEditNoteUseCase: AddEditNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _addEditState = mutableStateOf<AddEditNoteState>(AddEditNoteState())
    val addEditState: State<AddEditNoteState> = _addEditState

    private val _getNoteState = mutableStateOf<GetNoteState>(GetNoteState())
    val getNoteState: State<GetNoteState> = _getNoteState

    val titleFieldState = mutableStateOf("")
    val descriptionFieldState = mutableStateOf("")

    val dropDownMenuState = mutableStateOf(false)

    var noteColor: Int = 0

    init {
        val noteId: Int = savedStateHandle.get<String>("noteId")?.toInt() ?: -1
        if(noteId != -1) {
            loadNote(noteId)
        }
    }

    private fun loadNote(noteId: Int) {
        viewModelScope.launch {
            getNoteUseCase.execute(noteId).collect { result ->
                when (result) {
                    is Result.Success<Note> -> {
                        _getNoteState.value = GetNoteState(note = result.data ?: Note())
                        titleFieldState.value = _getNoteState.value.note!!.title
                        descriptionFieldState.value = _getNoteState.value.note!!.description
                        noteColor = _getNoteState.value.note!!.color
                    }
                    is Result.Loading<Note> -> {
                        _getNoteState.value = GetNoteState(isLoading = true)
                    }
                    is Result.Error<Note> -> {
                        _getNoteState.value = GetNoteState(error = result.message)
                    }
                }
            }
        }
    }

    fun saveNote(onSuccess: () -> Unit) {
        viewModelScope.launch {
            addEditNoteUseCase.execute(
                Note(
                    id = _getNoteState.value.note?.id ?: 0,
                    title = titleFieldState.value,
                    description = descriptionFieldState.value,
                    color = noteColor
                ),
            ).collect { result ->
                when (result) {
                    is Result.Success<Boolean> -> {
                        onSuccess()
                    }
                    is Result.Loading<Boolean> -> {
                        _addEditState.value = AddEditNoteState(isLoading = true)
                    }
                    is Result.Error<Boolean> -> {
                        _addEditState.value = AddEditNoteState(error = result.message)
                    }
                }
            }
        }
    }
}