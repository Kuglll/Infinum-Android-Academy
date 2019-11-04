package com.example.kuglll.shows_mark.utils

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService{

    @Headers(
        "Content-Type: application/json"
    )
    @POST("/api/users")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResult>

    @Headers(
        "Content-Type: application/json"
    )
    @POST("/api/users/sessions")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResult>

}

