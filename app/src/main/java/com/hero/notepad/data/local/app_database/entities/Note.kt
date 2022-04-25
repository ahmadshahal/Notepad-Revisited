package com.hero.notepad.data.local.app_database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val color: Int = 0,
)
