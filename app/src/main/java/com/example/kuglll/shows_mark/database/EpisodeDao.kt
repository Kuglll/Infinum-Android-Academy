package com.example.kuglll.shows_mark.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EpisodeDao{

    @Insert
    fun insert(episode: EpisodeTable)

    @Query("SELECT * FROM EpisodeTable")
    fun getEpisodes(): List<EpisodeTable>
}