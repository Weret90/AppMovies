package com.umbrella.appmovies.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.umbrella.appmovies.model.network.RetroInstance
import com.umbrella.appmovies.model.network.RetroService
import com.umbrella.appmovies.model.AppState
import com.umbrella.appmovies.model.Film
import com.umbrella.appmovies.model.FilmsList
import com.umbrella.appmovies.model.database.FilmsDatabase
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

    fun makeApiCall(genre1: String, genre2: String, genre3: String) {
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
            val retroInstance =
                RetroInstance.getRetroInstance().create(RetroService::class.java)
            val response = retroInstance.getDataFromApi(page, genre)
            response
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