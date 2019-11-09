package com.example.kuglll.shows_mark.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [EpisodeTable::class, ShowTable::class],
    exportSchema = false
)

abstract class Database: RoomDatabase(){

    abstract fun episodeDao(): EpisodeDao

    abstract fun showDao(): ShowDao

}