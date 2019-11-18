package com.example.kuglll.shows_mark.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ShowDao{
    @Insert
    fun insert(show: ShowTable)

    @Query("SELECT * FROM ShowTable")
    fun getShows(): List<ShowTable>

    @Query("SELECT id FROM ShowTable WHERE id = :id")
    fun getShowById(id: String): String

    @Query("DELETE FROM ShowTable")
    fun deleteAll()

    @Query("UPDATE ShowTable SET description = :description WHERE id = :id")
    fun updateDescription(description: String, id: String)

    @Query("SELECT description FROM ShowTable WHERE id = :id")
    fun getDescriptionByShowId(id: String): String

    @Query("SELECT likesCount FROM ShowTable WHERE id = :id")
    fun getLikesCountByShowId(id: String): Int

    @Query("SELECT title FROM ShowTable WHERE id = :id")
    fun getTitleByShowId(id: String): String

    @Query("UPDATE ShowTable SET likeStatus = :likeStatus WHERE id = :id")
    fun updateLikeStatus(likeStatus: Boolean?, id: String)

    @Query("SELECT likeStatus FROM ShowTable WHERE id = :id")
    fun getLikeStatusByShowId(id: String): Boolean?



}