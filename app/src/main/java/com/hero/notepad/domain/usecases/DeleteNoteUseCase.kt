package com.hero.notepad.domain.usecases

import com.hero.notepad.common.Result
import com.hero.notepad.data.local.app_database.entities.Note
import com.hero.notepad.data.repositories.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(private val notesRepository: NotesRepository) {
    fun execute(note: Note) : Flow<Result<List<Note>>> = flow {
        try {
            emit(Result.Loading<List<Note>>())
            notesRepository.delete(note)
            val list = notesRepository.getAll()
            // kotlinx.coroutines.delay(3000)
            emit(Result.Success<List<Note>>(data = list))
        } catch (e: Exception) {
            emit(Result.Error<List<Note>>(message = e.localizedMessage ?: "An error has occurred"))
        }
    }
}