package com.hero.notepad.data.repositories

import com.hero.notepad.data.local.app_database.AppDatabase
import com.hero.notepad.data.local.app_database.entities.Note
import javax.inject.Inject

class NotesRepository @Inject constructor(private val appDb: AppDatabase) {
    suspend fun insert(note: Note) {
        appDb.notesDao().insert(note)
    }

    suspend fun delete(note: Note) {
        appDb.notesDao().delete(note)
    }

    suspend fun getById(id: Int): Note {
        return appDb.notesDao().getById(id)
    }

    suspend fun getAll(): List<Note> {
        return appDb.notesDao().getAll()
    }

    suspend fun update(note: Note) {
        appDb.notesDao().update(note)
    }
}