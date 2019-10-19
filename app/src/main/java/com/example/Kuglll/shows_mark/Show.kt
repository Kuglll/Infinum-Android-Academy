package com.example.Kuglll.shows_mark

data class Show(
    val ID: Int,
    val name: String,
    val start_date: Int,
    val end_date: Int,
    val imageID: Int,
    val episodes: MutableList<String> = mutableListOf<String>()
)