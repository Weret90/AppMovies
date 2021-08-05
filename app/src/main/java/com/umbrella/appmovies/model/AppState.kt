package com.umbrella.appmovies.model

sealed class AppState {
    object Success : AppState()
    class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}