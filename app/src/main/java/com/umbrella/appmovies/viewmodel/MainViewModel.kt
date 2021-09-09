package com.umbrella.appmovies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.umbrella.appmovies.model.AppState
import com.umbrella.appmovies.model.Film
import com.umbrella.appmovies.model.FilmsList
import com.umbrella.appmovies.model.Note
import com.umbrella.appmovies.model.database.FilmsDatabase
import com.umbrella.appmovies.model.database.NotesDatabase
import com.umbrella.appmovies.model.network.RetroInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseFavouriteFilms = FilmsDatabase.getInstance(application)
    private val databaseNotes = NotesDatabase.getInstance(application)

    private val downloadStatusLiveData = MutableLiveData<AppState>()
    private val databaseFavouriteFilmsLiveData = databaseFavouriteFilms.filmsDao().getAllFilms()
    private val databaseNotesLiveData = databaseNotes.notesDao().getAllNotes()

    fun getDownloadStatusLiveData() = downloadStatusLiveData
    fun getDatabaseFavouriteFilmsLiveData(): LiveData<List<Film>> = databaseFavouriteFilmsLiveData
    fun getDatabaseNotesLiveData(): LiveData<List<Note>> = databaseNotesLiveData

    fun makeApiCalls(genre1: String, genre2: String, genre3: String, includeAdult: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            downloadStatusLiveData.postValue(AppState.Loading)
            val allFilmsList = awaitAll(
                async { getOneCategoryFilms("1", genre1, includeAdult) },
                async { getOneCategoryFilms("1", genre2, includeAdult) },
                async { getOneCategoryFilms("1", genre3, includeAdult) },
            )
            try {
                downloadStatusLiveData.postValue(AppState.Success(allFilmsList.requireNoNulls()))
            } catch (e: Exception) {
                downloadStatusLiveData.postValue(AppState.Error(e))
            }
        }
    }

    private suspend fun getOneCategoryFilms(
        page: String,
        genre: String,
        includeAdult: Boolean
    ): FilmsList? {
        return try {
            RetroInstance.getFilmsDetailsFromServer(page, genre, includeAdult)
        } catch (e: Exception) {
            null
        }
    }

    fun deleteAllFilmsFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseFavouriteFilms.filmsDao().deleteAllFilms()
        }
    }

    fun insertFilmIntoDB(film: Film) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseFavouriteFilms.filmsDao().insertFilm(film)
        }
    }

    fun deleteFilmFromDB(film: Film) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseFavouriteFilms.filmsDao().deleteFilm(film)
        }
    }

    fun deleteNoteFromDB(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseNotes.notesDao().deleteNote(note)
        }
    }

    fun insertNoteIntoDB(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseNotes.notesDao().insertNote(note)
        }
    }
}