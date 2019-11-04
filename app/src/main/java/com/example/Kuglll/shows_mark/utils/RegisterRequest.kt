package com.example.Kuglll.shows_mark.utils

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
class RegisterRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") var password: String?
)
