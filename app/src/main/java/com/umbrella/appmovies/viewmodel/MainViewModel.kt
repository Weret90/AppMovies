package com.umbrella.appmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.appmovies.model.network.RetroInstance
import com.umbrella.appmovies.model.network.RetroService
import com.umbrella.appmovies.model.FilmsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val progressBarLiveData = MutableLiveData<String>()

    companion object {
        private val LOCK = Any()
        private var executedCoroutinesCounter = 0
        private const val mustBeExecutedNumber = 3
    }

    fun getProgressBarLiveData(): LiveData<String> {
        return progressBarLiveData
    }

    fun getFilmsLiveData(page: String, genre: String): LiveData<FilmsList> {
        val filmsLiveData = MutableLiveData<FilmsList>()
        viewModelScope.launch(Dispatchers.IO) {
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val response = retroInstance.getDataFromApi(page, genre)
            filmsLiveData.postValue(response)
            synchronized(LOCK) {
                executedCoroutinesCounter++
                if (executedCoroutinesCounter == mustBeExecutedNumber) {
                    executedCoroutinesCounter = 0
                    progressBarLiveData.postValue("download it's over")
                }
            }
        }
        return filmsLiveData
    }
}