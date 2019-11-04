package com.example.Kuglll.shows_mark

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService{

    @FormUrlEncoded
    @POST("/api/users")
    fun Register(@Field("email") email: String,
                 @Field("password") password: String): Call<RegisterResult>

}

