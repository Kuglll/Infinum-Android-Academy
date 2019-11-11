package com.example.kuglll.shows_mark.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EpisodeDao{

    @Insert
    fun insert(episode: EpisodeTable)

    @Query("SELECT * FROM EpisodeTable WHERE show_id = :showId")
    fun getEpisodesByShowId(showId: String): List<EpisodeTable>

    @Query("DELETE FROM EpisodeTable")
    fun deleteAll()

    @Query("SELECT id FROM EpisodeTable WHERE id = :id")
    fun getEpisodeById(id: String): String
}