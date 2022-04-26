package com.hero.notepad.domain.usecases

import com.hero.notepad.common.UiState
import com.hero.notepad.data.local.app_database.entities.Note
import com.hero.notepad.data.repositories.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class AddEditNoteUseCase @Inject constructor(private val notesRepository: NotesRepository) {
    fun execute(note: Note): Flow<UiState<Boolean>> = flow {
        try {
            emit(UiState.Loading<Boolean>())
            notesRepository.insert(note)
            // kotlinx.coroutines.delay(2000)
            // TODO: Fix Boolean isn't needed.
            emit(UiState.Success<Boolean>(data = true))
        } catch (e: Exception) {
            emit(UiState.Error<Boolean>(message = e.localizedMessage ?: "An error has occurred"))
        }
    }
}