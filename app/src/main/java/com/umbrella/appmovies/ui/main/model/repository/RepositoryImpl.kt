package com.umbrella.appmovies.ui.main.model.repository

import com.umbrella.appmovies.ui.main.model.pojo.Film

class RepositoryImpl : Repository {

    private val films = mutableListOf<Film>()

    override fun getFilmsFromServer(): List<Film> {
        for (i in 1..20) {
            films.add(
                Film(
                    "Фильм №$i",
                    "https://media.istockphoto.com/vectors/movie-and-film-poster-template-design-modern-retro-vintage-style-vector-id1060337220",
                    2020,
                    7.7,
                    "Здесь будет описание фильма №$i"
                )
            )
        }
        return films
    }

    override fun getFilmsFromLocalSource(): List<Film> {
        for (i in 1..20) {
            films.add(
                Film(
                    "Фильм №$i",
                    "https://media.istockphoto.com/vectors/movie-and-film-poster-template-design-modern-retro-vintage-style-vector-id1060337220",
                    2020,
                    7.7,
                    "Здесь будет описание фильма №$i"
                )
            )
        }
        return films
    }
}