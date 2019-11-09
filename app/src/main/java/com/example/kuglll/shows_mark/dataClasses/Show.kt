package com.example.kuglll.shows_mark.dataClasses

data class Show(
    val ID: Int,
    val name: String,
    val start_date: Int,
    val end_date: Int,
    val imageID: Int,
    var description: String = "Description missing!",
    var episodes: MutableList<Episode> = mutableListOf()
)