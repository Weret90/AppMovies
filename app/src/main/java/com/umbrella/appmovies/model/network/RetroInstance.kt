package com.umbrella.appmovies.model.network

import com.umbrella.appmovies.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroInstance {
    companion object {
        private const val baseUrl = "https://api.themoviedb.org/3/discover/"
        const val apiKey = BuildConfig.TMDB_API_KEY
        fun getRetroInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}