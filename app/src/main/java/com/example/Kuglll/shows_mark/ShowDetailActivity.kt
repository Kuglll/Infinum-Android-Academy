package com.example.Kuglll.shows_mark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class ShowDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)

        val title = intent.getStringExtra("toolbarTitle")
        toolbarTitle.text = title

        val episodes = mutableListOf<String>("Episode 1", "Episode 2", "Episode 3")

        episodesRecyclerView.layoutManager = LinearLayoutManager(this)
        episodesRecyclerView.adapter = EpisodesAdapter(episodes)
    }
}
