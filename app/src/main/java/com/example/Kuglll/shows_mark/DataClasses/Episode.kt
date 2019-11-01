package com.example.Kuglll.shows_mark.DataClasses

import android.graphics.drawable.Drawable

data class Episode(
    val title : String,
    val episode : Int,
    val season : Int,
    val description : String = "",
    var imageID : Drawable?  = null
)
