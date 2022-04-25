package com.hero.notepad.ui.screens.notes_list_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hero.notepad.common.Result
import com.hero.notepad.data.local.app_database.entities.Note
import com.hero.notepad.domain.usecases.DeleteNoteUseCase
import com.hero.notepad.domain.usecases.GetNotesUseCase
import com.hero.notepad.ui.screens.notes_list_screen.states.NotesListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _state: MutableState<NotesListState> = mutableStateOf(NotesListState())
    val state: State<NotesListState> = _state

    init {
        getNotes()
    }

    fun getNotes() {
        viewModelScope.launch {
            getNotesUseCase.execute().collect { result ->
                when (result) {
                    is Result.Success<List<Note>> -> {
                        _state.value = NotesListState(list = result.data ?: emptyList())
                    }
                    is Result.Loading -> {
                        _state.value = NotesListState(isLoading = true)
                    }
                    is Result.Error -> {
                        _state.value = NotesListState(error = result.message)
                    }
                }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase.execute(note).collect { result ->
                when (result) {
                    is Result.Success<List<Note>> -> {
                        _state.value = NotesListState(list = result.data ?: emptyList())
                    }
                    is Result.Loading -> {
                        _state.value = NotesListState(isLoading = true)
                    }
                    is Result.Error -> {
                        _state.value = NotesListState(error = result.message)
                    }
                }
            }
        }
    }
}