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

@JsonClass(generateAdapter = true)
class ShowResult(
    @Json(name = "data") val data: List<Show>
)

@JsonClass(generateAdapter = true)
class Show(
    @Json(name = "_id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "imageUrl") val imageUrl: String,
    @Json(name = "likesCount") val likesCount: Int
)

