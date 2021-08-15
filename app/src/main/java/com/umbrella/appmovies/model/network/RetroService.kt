package com.umbrella.appmovies.model.network

import com.umbrella.appmovies.model.FilmsList
import com.umbrella.appmovies.model.network.RetroInstance.Companion.apiKey
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroService {
    @GET("movie?api_key=$apiKey&sort_by=popularity.desc&language=ru-RU")
    suspend fun getDataFromApi(
        @Query("page") page: String,
        @Query("with_genres") genre: String
    ): FilmsList
}