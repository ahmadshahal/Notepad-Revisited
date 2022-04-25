package com.hero.notepad.domain.usecases

import com.hero.notepad.common.Result
import com.hero.notepad.data.local.app_database.entities.Note
import com.hero.notepad.data.repositories.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(private val notesRepository: NotesRepository) {
    fun execute(id: Int) : Flow<Result<Note>> = flow {
        try {
            emit(Result.Loading<Note>())
            val note = notesRepository.getById(id)
            // kotlinx.coroutines.delay(2000)
            emit(Result.Success<Note>(data = note))
        } catch (e: Exception) {
            emit(Result.Error<Note>(message = e.localizedMessage ?: "An error has occurred"))
        }
    }
}