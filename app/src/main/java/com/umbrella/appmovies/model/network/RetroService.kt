package com.umbrella.appmovies.model.network

import com.umbrella.appmovies.model.ActorDTO
import com.umbrella.appmovies.model.AllActorsDTO
import com.umbrella.appmovies.model.FilmsList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RetroService {
    @GET("3/discover/movie")
    suspend fun getDataFromApi(
        @Query("api_key") apiKey: String,
        @Query("sort_by") sortMethod: String,
        @Query("page") page: String,
        @Query("with_genres") genre: String,
        @Query("language") language: String,
        @Query("include_adult") includeAdult: Boolean
    ): FilmsList

    @GET("3/movie/{movie_id}/credits")
    suspend fun getActors(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): AllActorsDTO

    @GET("3/person/{person_id}")
    suspend fun getActorInfo(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String
    ): ActorDTO
}