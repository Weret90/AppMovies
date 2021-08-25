package com.umbrella.appmovies.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.umbrella.appmovies.model.Note

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("DELETE FROM notes")
    fun deleteAllNotes()

    @Insert
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)
}