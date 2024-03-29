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

    fun getShowDetailsById(id: String, callback: (String, Int, String) -> Unit){
        exectuor.execute{
            val description = database.showDao().getDescriptionByShowId(id)
            val likesCount = database.showDao().getLikesCountByShowId(id)
            val title = database.showDao().getTitleByShowId(id)
            callback(description, likesCount, title)
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

    fun updateLikeStatus(likeStatus: Boolean?, showId: String){
        exectuor.execute{
            database.showDao().updateLikeStatus(likeStatus, showId)
        }
    }

    fun getLikeStatusByShowId(id: String, callback: (Boolean?) -> Unit){
        exectuor.execute{
            val likeStatus = database.showDao().getLikeStatusByShowId(id)
            callback(likeStatus)
        }
    }

}