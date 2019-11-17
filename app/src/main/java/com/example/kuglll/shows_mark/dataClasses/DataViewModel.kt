package com.example.kuglll.shows_mark.dataClasses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
                //if(!call.isCanceled) getDataFromDatabase()
            }

            override fun onResponse(call: Call<ShowResult>, response: Response<ShowResult>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        shows.postValue(body.data)

                        //Repository.getShowById(Show.id){
                        //   if (it == null) addShowtoDatabase(Show)
                        //}

                    }
                }
            }
        })
    }

}