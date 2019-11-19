package com.example.kuglll.shows_mark.utils

import android.provider.MediaStore
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

@JsonClass(generateAdapter = true)
class ShowDetailResult(
    @Json(name = "data") val data: ShowDetail
)

@JsonClass(generateAdapter = true)
class ShowDetail(
    @Json(name = "_id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "likesCount") val likesCount: Int
)

@JsonClass(generateAdapter = true)
class EpisodeResult(
    @Json(name = "data") val data: List<Episode>
)

@JsonClass(generateAdapter = true)
class Episode(
    @Json(name = "_id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "imageUrl") val imageUrl: String,
    @Json(name = "episodeNumber") val episodeNumber: String,
    @Json(name = "season") val seasonNumber: String
)

@JsonClass(generateAdapter = true)
class MediaResult(
    @Json(name = "data") val data: Media
)

@JsonClass(generateAdapter = true)
class Media(
    @Json(name = "_id") val id: String
)


