package com.example.kuglll.shows_mark.dataClasses


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

    val shows = MutableLiveData<List<Show>>()

    fun loadShows(){
        Singleton.service.getShows().enqueue(object: Callback<ShowResult> {
            override fun onFailure(call: Call<ShowResult>, t: Throwable) {
                if(!call.isCanceled) getShowsFromDatabase()
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

}