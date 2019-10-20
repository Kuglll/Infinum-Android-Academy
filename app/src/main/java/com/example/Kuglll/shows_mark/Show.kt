package com.example.Kuglll.shows_mark

data class Show(
    val ID: Int,
    val name: String,
    val start_date: Int,
    val end_date: Int,
    val imageID: Int,
    val description: String = "Description missing!",
    val episodes: MutableList<String> = mutableListOf<String>()
)