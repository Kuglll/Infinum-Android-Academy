package com.example.kuglll.shows_mark.utils

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RegisterResult(
    @Json(name = "data") val data: Any
)

@JsonClass(generateAdapter = true)
class LoginResult(
    @Json(name = "data") val data: Token
)

@JsonClass(generateAdapter = true)
class Token(
    @Json(name = "token") val token: String
)

