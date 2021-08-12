package com.umbrella.appmovies.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.umbrella.appmovies.model.Film

@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class FilmsDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "films.db"
        private var database: FilmsDatabase? = null
        private val MONITOR = Any()

        fun getInstance(context: Context): FilmsDatabase {
            synchronized(MONITOR) {
                if (database == null) {
                    database =
                        Room.databaseBuilder(context, FilmsDatabase::class.java, DB_NAME).build()
                }
                return database!!
            }
        }
    }

    abstract fun filmsDao(): FilmsDao
}