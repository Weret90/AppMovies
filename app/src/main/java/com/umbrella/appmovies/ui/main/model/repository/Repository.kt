package com.umbrella.appmovies.ui.main.model.repository

import com.umbrella.appmovies.ui.main.model.pojo.Film

interface Repository {
    fun getFilmsFromServer(): List<Film>
    fun getFilmsFromLocalSource(): List<Film>
}