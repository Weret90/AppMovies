package com.umbrella.appmovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.appmovies.model.ActorDTO
import com.umbrella.appmovies.model.AllActorsDTO
import com.umbrella.appmovies.model.network.RetroInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActorsViewModel : ViewModel() {

    val allActorsLiveData = MutableLiveData<AllActorsDTO>()
    val actorLiveData = MutableLiveData<ActorDTO>()

    fun makeApiCallAndGetAllActors(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetroInstance.getActorsFromServer(movieId)
            allActorsLiveData.postValue(response)
        }
    }

    fun makeApiCalAndGetActorInfo(actorId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetroInstance.getActorInfoFromServer(actorId)
            actorLiveData.postValue(response)
        }
    }
}