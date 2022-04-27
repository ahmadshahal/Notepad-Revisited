package com.hero.notepad.ui.screens.notes_list_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hero.notepad.common.UiState
import com.hero.notepad.data.local.app_database.entities.Note
import com.hero.notepad.domain.usecases.AddEditNoteUseCase
import com.hero.notepad.domain.usecases.DeleteNoteUseCase
import com.hero.notepad.domain.usecases.GetNotesUseCase
import com.hero.notepad.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val addEditNoteUseCase: AddEditNoteUseCase,
) : ViewModel() {

    private val _screenState: MutableState<UiState<List<Note>>> =
        mutableStateOf(UiState.Initial<List<Note>>())
    val screenState: State<UiState<List<Note>>>
        get() = _screenState

    private var deletedNotes: MutableList<Note> = mutableListOf()

    private val _uiEvent: Channel<UiEvent> = Channel()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

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
        deletedNotes.add(note)
        viewModelScope.launch {
            deleteNoteUseCase.execute(note).collect { uiState ->
                _screenState.value = uiState
            }
            _uiEvent.send(UiEvent.ShowSnackBar(message = "Note Deleted", action = "Undo"))
        }
    }

    fun undoDeleteNote() {
        if (deletedNotes.isNotEmpty()) {
            viewModelScope.launch {
                addEditNoteUseCase.execute(deletedNotes.last()).collect { deleteState ->
                    when (deleteState) {
                        is UiState.Success -> {
                            getNotesUseCase.execute().collect { uiState ->
                                _screenState.value = uiState
                            }
                        }
                        is UiState.Loading -> {
                            _screenState.value = UiState.Loading()
                        }
                        is UiState.Error -> {
                            _screenState.value = UiState.Error(
                                message = deleteState.message ?: "An Error has Occurred"
                            )
                        }
                        else -> Unit
                    }
                }
                deletedNotes.removeLast()
            }
        }
    }
}