package com.example.kuglll.shows_mark.dataClasses


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kuglll.shows_mark.database.Repository
import com.example.kuglll.shows_mark.database.ShowTable
import com.example.kuglll.shows_mark.utils.Show
import com.example.kuglll.shows_mark.utils.ShowResult
import com.example.kuglll.shows_mark.utils.Singleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataViewModel : ViewModel(){

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val episodeInserted = MutableLiveData<Boolean>()

    val showDescription = MutableLiveData<String>()
    val likesCount = MutableLiveData<Int>()

    val likeStatus = MutableLiveData<Boolean>()

    val shows = MutableLiveData<List<Show>>()

    fun loadShows(){
        Singleton.service.getShows().enqueue(object: Callback<ShowResult> {
            override fun onFailure(call: Call<ShowResult>, t: Throwable) {
                getShowsFromDatabase()
            }

            override fun onResponse(call: Call<ShowResult>, response: Response<ShowResult>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        shows.postValue(body.data)

                        body.data.map { Show ->
                            Repository.getShowById(Show.id){
                               if (it == null) addShowtoDatabase(Show)
                            }
                        }

                    }
                }
            }
        })
    }

    fun addShowtoDatabase(show: Show){
        Repository.addShow(ShowTable(
            show.id,
            show.title,
            show.imageUrl,
            show.likesCount)
        )
    }

    fun getShowsFromDatabase(){
        val mapping = ArrayList<Show>()
        Repository.getShows{
            it.map {show ->
                mapping.add(Show(
                    show.id,
                    show.title,
                    show.imageUrl,
                    show.likesCount
                ))
            }
        }
        shows.postValue(mapping)
    }

    fun likeShow(showId: String, token: String?){
        Singleton.service.likeShow(showId, token).enqueue(object: Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("likeStatus", "Show liked successfully failed")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Log.d("likeStatus", "Show successfully liked")
                likesCount.value?.let{count ->
                    if(likeStatus.value == null){
                        likesCount.value = count + 1
                    } else{
                        likesCount.value = count + 2
                    }
                }
                likeStatus.value = true
                Repository.updateLikeStatus(true, showId)
            }
        })
    }

    fun dislikeShow(showId: String, token: String?){
        Singleton.service.dislikeShow(showId, token).enqueue(object: Callback<Unit>{
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("likeStatus", "Show disliked successfully failed")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Log.d("likeStatus", "Show successfully disliked")
                likesCount.value?.let{count ->
                    if(likeStatus.value == null){
                        likesCount.value = count - 1
                    } else{
                        likesCount.value = count - 2
                    }
                }
                likeStatus.value = false
                Repository.updateLikeStatus(false, showId)
            }
        })
    }

    fun checkDatabaseForLikeStatus(showId: String){
        Repository.getLikeStatusByShowId(showId){
            likeStatus.postValue(it)
        }
    }

}