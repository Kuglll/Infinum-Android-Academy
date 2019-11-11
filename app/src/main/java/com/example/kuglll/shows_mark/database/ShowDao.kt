package com.example.kuglll.shows_mark.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ShowDao{
    @Insert
    fun insert(show: ShowTable)

    @Query("SELECT * FROM ShowTable")
    fun getShows(): List<ShowTable>
}