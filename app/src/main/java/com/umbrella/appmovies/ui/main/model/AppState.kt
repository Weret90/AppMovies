package com.umbrella.appmovies.ui.main.model

import com.umbrella.appmovies.ui.main.model.pojo.Film

sealed class AppState {
    data class Success(val films: List<Film>) : AppState()
    class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}