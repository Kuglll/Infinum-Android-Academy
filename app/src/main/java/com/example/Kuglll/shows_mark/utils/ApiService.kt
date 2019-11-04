package com.example.Kuglll.shows_mark.utils

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService{

    @Headers(
        "Content-Type: application/json"
    )
    @POST("/api/users")
    fun Register(@Body registerRequest: RegisterRequest): Call<RegisterResult>

}

