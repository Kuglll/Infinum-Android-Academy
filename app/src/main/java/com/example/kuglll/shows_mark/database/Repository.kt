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

    fun getShowById(ID: String, callback: (String?) -> Unit){
        exectuor.execute{
            val id = database.showDao().getShowById(ID)
            callback(id)
        }
    }

    fun deleteAllShows(){
        exectuor.execute{
            database.showDao().deleteAll()
        }
    }

    fun updateDesctiption(description: String, id: String){
        exectuor.execute{
            database.showDao().updateDescription(description, id)
        }
    }

    fun getShowDetailsById(id: String, callback: (String, Int) -> Unit){
        exectuor.execute{
            val description = database.showDao().getDescriptionByShowId(id)
            val likesCount = database.showDao().getLikesCountByShowId(id)
            callback(description, likesCount)
        }
    }

    fun addEpisode(episode: EpisodeTable){
        exectuor.execute{
            database.episodeDao().insert(episode)
        }
    }

    fun getEpisodesByShowId(showId: String, callback: (List<EpisodeTable>) -> Unit){
        exectuor.execute{
            val episodeList = database.episodeDao().getEpisodesByShowId(showId)
            callback(episodeList)
        }
    }

    fun deleteAllEpisodes(){
        exectuor.execute{
            database.episodeDao().deleteAll()
        }
    }

    fun getEpisodeById(id: String, callback: (String?) -> Unit){
        exectuor.execute{
            val id = database.episodeDao().getEpisodeById(id)
            callback(id)
        }
    }

}