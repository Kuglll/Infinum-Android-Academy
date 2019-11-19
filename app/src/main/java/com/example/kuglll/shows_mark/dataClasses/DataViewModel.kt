package com.example.kuglll.shows_mark.dataClasses


import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kuglll.shows_mark.database.EpisodeTable
import com.example.kuglll.shows_mark.database.Repository
import com.example.kuglll.shows_mark.database.ShowTable
import com.example.kuglll.shows_mark.utils.*
import com.example.kuglll.shows_mark.utils.Episode
import com.example.kuglll.shows_mark.utils.Show
import dmax.dialog.SpotsDialog
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DataViewModel : ViewModel(){

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val toolbarTitle = MutableLiveData<String>()
    val showDescription = MutableLiveData<String>()
    val likesCount = MutableLiveData<Int>()

    val likeStatus = MutableLiveData<Boolean>()

    val shows = MutableLiveData<List<Show>>()
    val episodes = MutableLiveData<List<Episode>>()

    fun loadShows(context: Context){
        val dialog: AlertDialog = SpotsDialog.Builder().setContext(context).build()
        dialog.show()
        Singleton.service.getShows().enqueue(object: Callback<ShowResult> {
            override fun onFailure(call: Call<ShowResult>, t: Throwable) {
                dialog.dismiss()
                getShowsFromDatabase()
            }

            override fun onResponse(call: Call<ShowResult>, response: Response<ShowResult>) {
                if (response.isSuccessful){
                    dialog.dismiss()
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

    fun likeShow(showId: String, token: String?, context: Context){
        val dialog: AlertDialog = SpotsDialog.Builder().setContext(context).build()
        dialog.show()
        Singleton.service.likeShow(showId, token).enqueue(object: Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                dialog.dismiss()
                Toast.makeText(context, "In order to like show you need internet access!", Toast.LENGTH_LONG).show()
                Log.d("likeStatus", "Show liked successfully failed")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                dialog.dismiss()
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

    fun dislikeShow(showId: String, token: String?, context: Context){
        val dialog: AlertDialog = SpotsDialog.Builder().setContext(context).build()
        dialog.show()
        Singleton.service.dislikeShow(showId, token).enqueue(object: Callback<Unit>{
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                dialog.dismiss()
                Toast.makeText(context, "In order to dislike show you need internet access!", Toast.LENGTH_LONG).show()
                Log.d("likeStatus", "Show disliked successfully failed")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                dialog.dismiss()
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

    fun fetchShowDetails(showId: String, context: Context){
        val dialog: AlertDialog = SpotsDialog.Builder().setContext(context).build()
        dialog.show()
        Singleton.service.getShowDetails(showId).enqueue(object : Callback<ShowDetailResult> {
            override fun onFailure(call: Call<ShowDetailResult>, t: Throwable) {
                dialog.dismiss()
                if(!call.isCanceled) {
                    Repository.getShowDetailsById(showId) { description, lCount, title ->
                        showDescription.postValue(description)
                        likesCount.postValue(lCount)
                        toolbarTitle.postValue(title)
                    }
                }
            }

            override fun onResponse(call: Call<ShowDetailResult>, response: Response<ShowDetailResult>) {
                dialog.dismiss()
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        Repository.updateDesctiption(body.data.description, body.data.id)
                        toolbarTitle.value = body.data.title
                        showDescription.value = body.data.description
                        likesCount.value = body.data.likesCount
                    }
                }
            }

        })
    }

    fun fetchEpisodes(showId: String, context: Context){
        val dialog: AlertDialog = SpotsDialog.Builder().setContext(context).build()
        dialog.show()
        Singleton.service.getShowEpisodes(showId).enqueue(object : Callback<EpisodeResult>{
            override fun onFailure(call: Call<EpisodeResult>, t: Throwable) {
                dialog.dismiss()
                getEpisodesFromDatabase(showId)
            }

            override fun onResponse(call: Call<EpisodeResult>, response: Response<EpisodeResult>) {
                Log.d("EPISODES FETCHED", "EPISODES FETCHED success")
                dialog.dismiss()
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        episodes.postValue(body.data)

                        body.data.map{ episode ->
                            Repository.getEpisodeById(episode.id){
                                if (it == null) addEpisodeToDatabase(showId, episode)
                            }
                        }
                    }
                }
            }

        })
    }

    fun getEpisodesFromDatabase(showId: String){
        val mapping = ArrayList<Episode>()
        Repository.getEpisodesByShowId(showId){
            it.map {episode ->
                mapping.add(Episode(
                    episode.id,
                    episode.title,
                    episode.description,
                    episode.imageUrl,
                    episode.episodeNumber,
                    episode.seasonNumber
                ))
            }
        }
        episodes.postValue(mapping)
    }

    fun addEpisodeToDatabase(showId: String, episode: Episode){
        Repository.addEpisode(EpisodeTable(
            showId,
            episode.id,
            episode.title,
            episode.description,
            episode.imageUrl,
            episode.episodeNumber,
            episode.seasonNumber
        ))
    }

    fun uploadMedia(imageFile: File, token: String?, request: EpisodeUploadRequest, context: Context){
        val dialog: AlertDialog = SpotsDialog.Builder().setContext(context).build()
        dialog.show()
        Singleton.service.uploadMedia(RequestBody.create(("image/jpg").toMediaType(), imageFile), token)
            .enqueue(object: Callback<MediaResult>{
                override fun onFailure(call: Call<MediaResult>, t: Throwable) {
                    dialog.dismiss()
                    Log.d("MEDIA UPLOAD", "MEDIA UPLOAD failed")
                }

                override fun onResponse(call: Call<MediaResult>, response: Response<MediaResult>) {
                    dialog.dismiss()
                    Log.d("MEDIA UPLOAD", "MEDIA UPLOAD success")
                    if(response.isSuccessful){
                        val body = response.body()
                        if(body != null){
                            request.mediaId = body.data.id
                            uploadEpisode(request, token, context)
                        }
                    }
                }
        })
    }

    fun uploadEpisode(request: EpisodeUploadRequest, token: String?, context: Context){
        val dialog: AlertDialog = SpotsDialog.Builder().setContext(context).build()
        dialog.show()
        Singleton.service.uploadEpisode(request, token).enqueue(object: Callback<Unit>{
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                dialog.dismiss()
                Log.d("EPISODE UPLOAD", "EPISODE UPLOAD failed")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                dialog.dismiss()
                Log.d("EPISODE UPLOAD", "EPISODE UPLOAD success")
            }

        })
    }

}
