package com.umbrella.appmovies.ui.main.model.pojo

import java.io.Serializable

data class Film(val title: String, val posterUrl: String, val year: Int, val rating: Double, val description: String) :
    Serializable
