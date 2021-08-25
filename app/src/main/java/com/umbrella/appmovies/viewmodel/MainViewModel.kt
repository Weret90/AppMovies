package com.umbrella.appmovies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.umbrella.appmovies.model.AppState
import com.umbrella.appmovies.model.Film
import com.umbrella.appmovies.model.FilmsList
import com.umbrella.appmovies.model.database.FilmsDatabase
import com.umbrella.appmovies.model.network.RetroInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FilmsDatabase.getInstance(application)

    private val networkLiveData = MutableLiveData<AppState>()
    private val databaseLiveData = database.filmsDao().getAllFilms()

    fun getNetworkLiveData() = networkLiveData

    fun getDatabaseLiveData(): LiveData<List<Film>> = databaseLiveData

    fun makeApiCalls(genre1: String, genre2: String, genre3: String) {
        viewModelScope.launch(Dispatchers.IO) {
            networkLiveData.postValue(AppState.Loading)
            val allFilmsList = awaitAll(
                async { getOneCategoryFilms("1", genre1) },
                async { getOneCategoryFilms("1", genre2) },
                async { getOneCategoryFilms("1", genre3) },
            )
            try {
                networkLiveData.postValue(AppState.Success(allFilmsList.requireNoNulls()))
            } catch (e: Exception) {
                networkLiveData.postValue(AppState.Error(e))
            }
        }
    }

    private suspend fun getOneCategoryFilms(page: String, genre: String): FilmsList? {
        return try {
            RetroInstance.getFilmsDetailsFromServer(page, genre)
        } catch (e: Exception) {
            null
        }
    }

    fun deleteAllFilmsFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            database.filmsDao().deleteAllFilms()
        }
    }

    fun insertFilmFromDB(film: Film) {
        viewModelScope.launch(Dispatchers.IO) {
            database.filmsDao().insertFilm(film)
        }
    }

    fun deleteFilmFromDB(film: Film) {
        viewModelScope.launch(Dispatchers.IO) {
            database.filmsDao().deleteFilm(film)
        }
    }

    fun updateFilmFromDB(film: Film) {
        viewModelScope.launch(Dispatchers.IO) {
            database.filmsDao().updateFilm(film)
        }
    }
}