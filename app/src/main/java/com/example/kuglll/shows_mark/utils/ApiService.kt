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

    @GET("/api/shows/{showID}")
    fun getShowDetails(@Path("showID") showID : String): Call<ShowDetailResult>

    @GET("/api/shows/{showID}/episodes")
    fun getShowEpisodes(@Path("showID") showID : String): Call<EpisodeResult>

    @POST("/api/shows/{showId}/like")
    fun likeShow(@Path("showId") showId: String, @Header("Authorization") token: String?): Call<Unit>

    @POST("/api/shows/{showId}/dislike")
    fun dislikeShow(@Path("showId") showId: String, @Header("Authorization") token: String?): Call<Unit>

    @POST("/api/media")
    @Multipart
    fun uploadMedia(@Part("file\"; filename=\"image.jpg\"") request: RequestBody, @Header("Authorization") token: String?): Call<MediaResult>

    @POST("/api/episodes")
    fun uploadEpisode(@Body episodeUploadRequest: EpisodeUploadRequest): Call<Unit>




}

