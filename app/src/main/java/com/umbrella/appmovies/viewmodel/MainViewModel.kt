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
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val mainLiveData = MutableLiveData<AppState>()
    private val database = FilmsDatabase.getInstance(application)
    private val databaseLiveData = database.filmsDao().getAllFilms()

    companion object {
        private val LOCK = Any()
        private var executedCoroutinesCounter = 0
        private const val MUST_BE_EXECUTED = 3
    }

    fun getMainLiveData() = mainLiveData

    fun getDatabaseLiveData(): LiveData<List<Film>> {
        return databaseLiveData
    }

    fun getFilmsLiveData(page: String, genre: String): LiveData<FilmsList> {
        val filmsLiveData = MutableLiveData<FilmsList>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mainLiveData.postValue(AppState.Loading)
                val retroInstance =
                    RetroInstance.getRetroInstance().create(RetroService::class.java)
                val response = retroInstance.getDataFromApi(page, genre)
                filmsLiveData.postValue(response)
                synchronized(LOCK) {
                    executedCoroutinesCounter++
                    if (executedCoroutinesCounter == MUST_BE_EXECUTED) {
                        mainLiveData.postValue(AppState.Success)
                        executedCoroutinesCounter = 0
                    }
                }
            } catch (e: Exception) {
                mainLiveData.postValue(AppState.Error(e))
            }
        }
        return filmsLiveData
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