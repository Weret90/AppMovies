package com.umbrella.appmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.appmovies.model.network.RetroInstance
import com.umbrella.appmovies.model.network.RetroService
import com.umbrella.appmovies.model.network.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val progressBarLiveData = MutableLiveData<String>()

    companion object {
        private val LOCK = Any()
        private var executedCoroutinesCounter = 0
        private const val MUST_BE_EXECUTED = 3
        const val DOWNLOAD_ERROR = "download error"
        const val DOWNLOAD_SUCCESS = "download it's over"
    }

    fun getProgressBarLiveData() = progressBarLiveData

    fun getFilmsLiveData(page: String, genre: String): LiveData<AppState> {
        val filmsLiveData = MutableLiveData<AppState>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val retroInstance =
                    RetroInstance.getRetroInstance().create(RetroService::class.java)
                val response = retroInstance.getDataFromApi(page, genre)
                filmsLiveData.postValue(AppState.Success(response))
                checkAllCoroutinesWorkStatus()

            } catch (e: Exception) {
                filmsLiveData.postValue(AppState.Error(e))
                progressBarLiveData.postValue(DOWNLOAD_ERROR)
            }
        }
        return filmsLiveData
    }

    private fun checkAllCoroutinesWorkStatus() {
        synchronized(LOCK) {
            executedCoroutinesCounter++
            if (isAllCoroutinesHasBeenExecuted()) {
                progressBarLiveData.postValue(DOWNLOAD_SUCCESS)
            }
        }
    }

    fun isAllCoroutinesHasBeenExecuted() = executedCoroutinesCounter == MUST_BE_EXECUTED

    fun resetExecutedCoroutinesCounter() {
        executedCoroutinesCounter = 0
    }
}