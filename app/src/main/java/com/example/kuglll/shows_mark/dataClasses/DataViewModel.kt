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

    val episodeImage = MutableLiveData<String>()
    val episodeTitle = MutableLiveData<String>()
    val episodeDescription = MutableLiveData<String>()
    val episodeSeasonNumber = MutableLiveData<String>()

    val comments = MutableLiveData<List<Comment>>()

    fun loadShows(onStart: () -> Unit, onStop: (Boolean) -> Unit){
        onStart()
        Singleton.service.getShows().enqueue(object: Callback<ShowResult> {
            override fun onFailure(call: Call<ShowResult>, t: Throwable) {
                onStop(true)
                getShowsFromDatabase()
            }

            override fun onResponse(call: Call<ShowResult>, response: Response<ShowResult>) {
                onStop(false)
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

    fun likeShow(showId: String, token: String?, onStart: () -> Unit, onStop: (Boolean) -> Unit){
        onStart()
        Singleton.service.likeShow(showId, token).enqueue(object: Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onStop(true)
                Log.d("likeStatus", "Show liked successfully failed")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                onStop(false)
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

    fun dislikeShow(showId: String, token: String?, onStart: () -> Unit, onStop: (Boolean) -> Unit){
        onStart()
        Singleton.service.dislikeShow(showId, token).enqueue(object: Callback<Unit>{
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onStop(true)
                Log.d("likeStatus", "Show disliked successfully failed")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                onStop(false)
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

    fun fetchShowDetails(showId: String, onStop: (Boolean) -> Unit){
        Singleton.service.getShowDetails(showId).enqueue(object : Callback<ShowDetailResult> {
            override fun onFailure(call: Call<ShowDetailResult>, t: Throwable) {
                onStop(true)
                if(!call.isCanceled) {
                    Repository.getShowDetailsById(showId) { description, lCount, title ->
                        showDescription.postValue(description)
                        likesCount.postValue(lCount)
                        toolbarTitle.postValue(title)
                    }
                }
            }

            override fun onResponse(call: Call<ShowDetailResult>, response: Response<ShowDetailResult>) {
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

    fun fetchEpisodes(showId: String, onStart: () -> Unit, onStop: (Boolean) -> Unit){
        onStart()
        Singleton.service.getShowEpisodes(showId).enqueue(object : Callback<EpisodeResult>{
            override fun onFailure(call: Call<EpisodeResult>, t: Throwable) {
                onStop(true)
                getEpisodesFromDatabase(showId)
            }

            override fun onResponse(call: Call<EpisodeResult>, response: Response<EpisodeResult>) {
                Log.d("EPISODES FETCHED", "EPISODES FETCHED success")
                onStop(false)
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
        Log.d("EPISODES FETCHED", "EPISODES FETCHED FROM DB success")
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

    fun uploadMedia(imageFile: File, token: String?, request: EpisodeUploadRequest, onStart: () -> Unit, onResponse: (String) -> Unit, onFailure: () -> Unit){
        onStart()
        Singleton.service.uploadMedia(RequestBody.create(("image/jpg").toMediaType(), imageFile), token)
            .enqueue(object: Callback<MediaResult>{
                override fun onFailure(call: Call<MediaResult>, t: Throwable) {
                    onFailure()
                    Log.d("MEDIA UPLOAD", "MEDIA UPLOAD failed")
                }

                override fun onResponse(call: Call<MediaResult>, response: Response<MediaResult>) {
                    Log.d("MEDIA UPLOAD", "MEDIA UPLOAD success")
                    if(response.isSuccessful){
                        val body = response.body()
                        if(body != null){
                            onResponse(body.data.id)
                        }
                    }
                }
        })
    }

    fun uploadEpisode(request: EpisodeUploadRequest, token: String?, onStart: () -> Unit, onResponse: () -> Unit, onFailure: () -> Unit){
        onStart()
        Singleton.service.uploadEpisode(request, token).enqueue(object: Callback<Unit>{
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onFailure()
                Log.d("EPISODE UPLOAD", "EPISODE UPLOAD failed")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                onResponse()
                Log.d("EPISODE UPLOAD", "EPISODE UPLOAD success")
            }

        })
    }

    fun getEpisodeDetails(episdeId: String, onStart: () -> Unit, onStop: (Boolean) -> Unit){
        onStart()
        Singleton.service.getEpisodeDetails(episdeId).enqueue(object: Callback<EpisodeDetailResult>{
            override fun onFailure(call: Call<EpisodeDetailResult>, t: Throwable) {
                onStop(true)
                Log.d("EPISODE DETAIL", "EPISODE DETAIL FETCH failed")
            }

            override fun onResponse(call: Call<EpisodeDetailResult>, response: Response<EpisodeDetailResult>) {
                onStop(false)
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        val episodeSeason = String.format("%s %s", body.data.episodeNumber, body.data.seasonNumber)
                        episodeImage.postValue(body.data.image)
                        episodeTitle.postValue(body.data.title)
                        episodeDescription.postValue(body.data.description)
                        episodeSeasonNumber.postValue(episodeSeason)
                    }
                }
            }

        })
    }

    fun getEpisodeComments(episodeId: String, onStart: () -> Unit, onStop: (Boolean) -> Unit){
        onStart()
        Singleton.service.getEpisodeComments(episodeId).enqueue(object: Callback<CommentsResult>{
            override fun onFailure(call: Call<CommentsResult>, t: Throwable) {
                onStop(true)
            }

            override fun onResponse(call: Call<CommentsResult>, response: Response<CommentsResult>) {
                onStop(false)
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        comments.postValue(body.data)
                    }
                }
            }

        })
    }

}
