package com.hero.notepad.domain.usecases

import com.hero.notepad.common.Result
import com.hero.notepad.data.local.app_database.entities.Note
import com.hero.notepad.data.repositories.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class AddEditNoteUseCase @Inject constructor(private val notesRepository: NotesRepository) {
    fun execute(note: Note): Flow<Result<Boolean>> = flow {
        try {
            emit(Result.Loading<Boolean>())
            notesRepository.insert(note)
            // kotlinx.coroutines.delay(3000)
            emit(Result.Success<Boolean>(data = true))
        } catch (e: Exception) {
            emit(Result.Error<Boolean>(message = e.localizedMessage ?: "An error has occurred"))
        }
    }
}