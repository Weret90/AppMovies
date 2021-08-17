package com.umbrella.appmovies.service

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.umbrella.appmovies.model.FilmsList
import com.umbrella.appmovies.model.network.RetroInstance
import com.umbrella.appmovies.model.network.RetroService
import com.umbrella.appmovies.view.fragments.*
import com.umbrella.appmovies.view.fragments.FilmsFragment.Companion.ACTION
import com.umbrella.appmovies.view.fragments.FilmsFragment.Companion.COMEDY
import com.umbrella.appmovies.view.fragments.FilmsFragment.Companion.HORROR
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class DetailsService(name: String = "DetailService") : IntentService(name) {
    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            loadMovies()
        }
    }

    private fun loadMovies() {
        GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val allFilmsList = awaitAll(
                async { getCategory("1", HORROR) },
                async { getCategory("1", ACTION) },
                async { getCategory("1", COMEDY) },
            )
            try {
                onSuccessResponse(allFilmsList.requireNoNulls())
            } catch (e: Exception) {
                onErrorResponse()
            }
        }
    }

    private suspend fun getCategory(page: String, genre: String): FilmsList? {
        return try {
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val response = retroInstance.getDataFromApi(page, genre)
            response
        } catch (e: Exception) {
            null
        }
    }

    private fun onSuccessResponse(allFilms: List<FilmsList>) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_FILMS_LIST_1, allFilms[0])
        broadcastIntent.putExtra(DETAILS_FILMS_LIST_2, allFilms[1])
        broadcastIntent.putExtra(DETAILS_FILMS_LIST_3, allFilms[2])
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onErrorResponse() {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, DETAILS_RESPONSE_ERROR_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyIntent() {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, DETAILS_INTENT_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
}