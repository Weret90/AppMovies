package com.umbrella.appmovies.model

sealed class AppState {
    data class Success(val films: List<FilmsList>) : AppState()
    class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}