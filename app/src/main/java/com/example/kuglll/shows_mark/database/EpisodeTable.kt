package com.example.kuglll.shows_mark.database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ShowTable::class,
            parentColumns = ["id"],
            childColumns = ["show_id"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("show_id")]
)
data class EpisodeTable(
    @ColumnInfo(name = "show_id")
    val showId: String,
    @PrimaryKey val id: String,

    val title: String,
    val description: String,
    val imageUrl: String,
    val episodeNumber: String,
    val seasonNumber: String
)