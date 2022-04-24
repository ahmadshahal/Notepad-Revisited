package com.hero.notepad.data.local.app_database.daos

import androidx.room.*
import com.hero.notepad.data.local.app_database.entities.Note

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM Note")
    suspend fun getAll(): List<Note>

    @Query("SELECT * FROM Note WHERE id = :id")
    suspend fun getById(id: Int): Note

    @Update
    suspend fun update(note: Note)
}