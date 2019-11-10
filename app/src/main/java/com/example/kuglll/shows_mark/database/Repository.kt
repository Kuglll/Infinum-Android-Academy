package com.example.kuglll.shows_mark.database

import androidx.room.Room
import com.example.kuglll.shows_mark.utils.AppInstance
import com.example.kuglll.shows_mark.utils.Show
import java.util.concurrent.Executors

object Repository{

    val database = Room.databaseBuilder(
        AppInstance.instance,
        MyDatabase::class.java,
        "mydb"
    ).fallbackToDestructiveMigration()
    .build()

    private val exectuor = Executors.newSingleThreadExecutor()

    fun addShow(show: ShowTable){
        exectuor.execute{
            database.showDao().insert(show)
        }
    }

    fun getShows(callback: (List<ShowTable>) -> Unit) {
        exectuor.execute{
            val showList = database.showDao().getShows()
            callback(showList.toList())
        }
    }

}