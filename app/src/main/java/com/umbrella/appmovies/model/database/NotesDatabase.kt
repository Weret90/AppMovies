package com.umbrella.appmovies.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.umbrella.appmovies.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {
    companion object {
        private const val DB_NAME = "notes.db"
        private var database: NotesDatabase? = null
        private val MONITOR = Any()

        fun getInstance(context: Context): NotesDatabase {
            synchronized(MONITOR) {
                if (database == null) {
                    database =
                        Room.databaseBuilder(context, NotesDatabase::class.java, DB_NAME).build()
                }
                return database!!
            }
        }
    }

    abstract fun notesDao(): NotesDao
}