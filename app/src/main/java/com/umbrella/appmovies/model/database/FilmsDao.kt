package com.umbrella.appmovies.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.umbrella.appmovies.model.Film

@Dao
interface FilmsDao {
    @Query("SELECT * FROM `selected films`")
    fun getAllFilms(): LiveData<List<Film>>

    @Query("DELETE FROM `selected films`")
    fun deleteAllFilms()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilm(film: Film)

    @Delete
    fun deleteFilm(film: Film)

    @Update
    fun updateFilm(film: Film)
}