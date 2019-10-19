package com.example.Kuglll.shows_mark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ShowActivity : AppCompatActivity() {

    val shows = mutableListOf<Show>(
        Show()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
    }
}
