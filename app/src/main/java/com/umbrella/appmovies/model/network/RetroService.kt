package com.umbrella.appmovies.model.network

import com.umbrella.appmovies.model.FilmsList
import retrofit2.http.GET
import retrofit2.http.Query



interface RetroService {
    @GET("3/discover/movie")
    suspend fun getDataFromApi(
        @Query("api_key") apiKey: String,
        @Query("sort_by") sortMethod: String,
        @Query("page") page: String,
        @Query("with_genres") genre: String,
        @Query("language") language: String,
    ): FilmsList
}