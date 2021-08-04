package com.umbrella.appmovies.model.network

import com.umbrella.appmovies.model.FilmsList

sealed class AppState {
    data class Success(val response: FilmsList) : AppState()
    class Error(val error: Throwable) : AppState()
}