package com.example.kuglll.shows_mark.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ShowTable::class,
            parentColumns = ["id"],
            childColumns = ["show_id"],
            onDelete = CASCADE
        )
    ]
)
data class EpisodeTable(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "show_id")
    val showId: String,

    val title: String,
    val description: String,
    val imageUrl: String,
    val episodeNumber: String,
    val seasonNumber: String
)