package com.umbrella.appmovies.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    val filmTitle: String,
    val dateOfWatching: String,
    val review: String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}
