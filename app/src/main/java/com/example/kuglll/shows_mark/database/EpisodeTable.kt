package com.example.kuglll.shows_mark.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EpisodeTable(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val episodeNumber: String,
    val seasonNumber: String
)