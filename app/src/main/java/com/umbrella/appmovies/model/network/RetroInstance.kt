package com.umbrella.appmovies.model.network

import com.umbrella.appmovies.BuildConfig
import com.umbrella.appmovies.model.FilmsList
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseUrl = "https://api.themoviedb.org/"
private const val apiKey = BuildConfig.TMDB_API_KEY
private const val sortMethod = "popularity.desc"
private const val language = "ru-RU"

class RetroInstance {
    companion object {
        private val apiFilms = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetroService::class.java)

        suspend fun getFilmsDetailsFromServer(page: String, genre: String): FilmsList {
            return apiFilms.getDataFromApi(apiKey, sortMethod, page, genre, language)
        }
    }
}