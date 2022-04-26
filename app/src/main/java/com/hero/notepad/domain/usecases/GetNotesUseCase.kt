package com.hero.notepad.domain.usecases

import com.hero.notepad.data.local.app_database.entities.Note
import com.hero.notepad.data.repositories.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject
import com.hero.notepad.common.UiState

class GetNotesUseCase @Inject constructor(private val notesRepository: NotesRepository) {
    fun execute() : Flow<UiState<List<Note>>> = flow {
        try {
            emit(UiState.Loading<List<Note>>())
            val list = notesRepository.getAll()
            // kotlinx.coroutines.delay(2000)
            emit(UiState.Success<List<Note>>(data = list))
        } catch (e: Exception) {
            emit(UiState.Error<List<Note>>(message = e.localizedMessage ?: "An error has occurred"))
        }
    }
}