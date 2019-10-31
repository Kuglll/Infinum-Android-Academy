package com.example.Kuglll.shows_mark

import android.graphics.drawable.Drawable

data class Episode(
    val title : String,
    val description : String = "",
    var imageID : Drawable?  = null
)
