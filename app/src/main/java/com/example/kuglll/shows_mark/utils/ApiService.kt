package com.example.kuglll.shows_mark.utils

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService{

    @Headers(
        "Content-Type: application/json"
    )
    @POST("/api/users")
    fun register(@Body registerRequest: RegisterRequest): Call<Any>

    @Headers(
        "Content-Type: application/json"
    )
    @POST("/api/users/sessions")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResult>

    @GET("/api/shows")
    fun getShows(): Call<ShowResult>

    @GET("/api/shows/{showId}")
    fun getShowDetails(@Path("showId") showId: String): Call<ShowDetailResult>

    @GET("/api/shows/{showId}/episodes")
    fun getShowEpisodes(@Path("showId") showId: String): Call<EpisodeResult>

    @POST("/api/shows/{showId}/like")
    fun likeShow(@Path("showId") showId: String, @Header("Authorization") token: String?): Call<Unit>

    @POST("/api/shows/{showId}/dislike")
    fun dislikeShow(@Path("showId") showId: String, @Header("Authorization") token: String?): Call<Unit>

    @POST("/api/media")
    @Multipart
    fun uploadMedia(@Part("file\"; filename=\"image.jpg\"") request: RequestBody, @Header("Authorization") token: String?): Call<MediaResult>

    @POST("/api/episodes")
    fun uploadEpisode(@Body episodeUploadRequest: EpisodeUploadRequest, @Header("Authorization") token: String?): Call<Unit>

    @GET("/api/episodes/{episodeId}")
    fun getEpisodeDetails(@Path("episodeId") episodeId: String): Call<EpisodeDetailResult>

    @GET("/api/episodes/{episodeId}/comments")
    fun getEpisodeComments(@Path("episodeId") episodeId: String): Call<CommentsResult>

    @POST("/api/comments")
    fun uploadComment(@Body commentUploadRequest: CommentUploadRequest, @Header("Authorization") token: String?): Call<Unit>

}

