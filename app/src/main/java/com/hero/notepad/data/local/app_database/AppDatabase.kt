package com.hero.notepad.data.local.app_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hero.notepad.data.local.app_database.daos.NotesDao
import com.hero.notepad.data.local.app_database.entities.Note

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}