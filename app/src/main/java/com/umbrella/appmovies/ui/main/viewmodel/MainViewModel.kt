package com.umbrella.appmovies.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umbrella.appmovies.ui.main.model.AppState
import com.umbrella.appmovies.ui.main.model.repository.RepositoryImpl

class MainViewModel : ViewModel() {

    private val repository = RepositoryImpl()
    private val liveDataToObserver = MutableLiveData<AppState>()

    fun getData(): LiveData<AppState> {
        return liveDataToObserver
    }

    fun getFilmsFromServer() {
        liveDataToObserver.value = AppState.Loading
        Thread {
            Thread.sleep(2000)
            val random = (1..3).random()  //вручную генерируем возможность ошибки скачивания данных с сервера, вероятность ошибки 33%
            if (random == 3) {
                liveDataToObserver.postValue(AppState.Error(Throwable()))
            } else {
                liveDataToObserver.postValue(AppState.Success(repository.getFilmsFromServer()))
            }
        }.start()
    }

//    fun getFilmsFromLocalSource() {
//        liveDataToObserver.value = AppState.Loading
//        Thread {
//            Thread.sleep(2000)
//            val random = (1..2).random()
//            if (random == 2) {
//                liveDataToObserver.postValue(AppState.Error(Throwable()))
//            } else {
//                liveDataToObserver.postValue(AppState.Success(repository.getFilmsFromLocalSource()))
//            }
//        }.start()
//    }
}