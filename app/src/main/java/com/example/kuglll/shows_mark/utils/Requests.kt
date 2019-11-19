package com.example.kuglll.shows_mark.utils

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RegisterRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
class EpisodeUploadRequest(
        @Json(name = "showId") val showId: String,
        @Json(name = "mediaId") val mediaId: String,
        @Json(name = "title") val title: String,
        @Json(name = "description") val description: String,
        @Json(name = "episodeNumber") val episodeNumber: String,
        @Json(name = "season") val season: String
)

