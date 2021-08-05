package com.umbrella.appmovies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.appmovies.model.network.RetroInstance
import com.umbrella.appmovies.model.network.RetroService
import com.umbrella.appmovies.model.AppState
import com.umbrella.appmovies.model.FilmsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val mainLiveData = MutableLiveData<AppState>()

    companion object {
        private val LOCK = Any()
        private var executedCoroutinesCounter = 0
        private const val MUST_BE_EXECUTED = 3
    }

    fun getMainLiveData() = mainLiveData

    fun getFilmsLiveData(page: String, genre: String): LiveData<FilmsList> {
        val filmsLiveData = MutableLiveData<FilmsList>()
        mainLiveData.postValue(AppState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val retroInstance =
                    RetroInstance.getRetroInstance().create(RetroService::class.java)
                val response = retroInstance.getDataFromApi(page, genre)
                filmsLiveData.postValue(response)
                synchronized(LOCK) {
                    executedCoroutinesCounter++
                    if (executedCoroutinesCounter == MUST_BE_EXECUTED) {
                        Log.i("proverka", "shansua")
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
}