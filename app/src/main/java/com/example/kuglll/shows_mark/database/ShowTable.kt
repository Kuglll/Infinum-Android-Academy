package com.example.kuglll.shows_mark.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShowTable(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String,
    val likesCount: Int
)