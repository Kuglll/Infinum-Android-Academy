package com.example.Kuglll.shows_mark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class ShowDetailActivity : AppCompatActivity() {

    var showID = 0
    //there must be a better way of initializing empty list
    var episodes : MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)

        val title = intent.getStringExtra("toolbarTitle")
        toolbarTitle.text = title

        showID = intent.getIntExtra("showID", -1)
        episodes = ShowActivity.storage.shows[showID].episodes

        showDescription.text = ShowActivity.storage.shows[showID].description
        floatingButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                startAddEpisodeActivity()
            }
        })

        episodesRecyclerView.layoutManager = LinearLayoutManager(this)
        episodesRecyclerView.adapter = EpisodesAdapter(episodes)


    }

    override fun onResume() {
        super.onResume()
        println("on resume")

        if(episodes.size > 0){
            episodesRecyclerView.visibility = View.VISIBLE
            iconSleep.visibility = View.GONE
            fallenAsleepTextView.visibility = View.GONE
            fallenAsleepTextView2.visibility = View.GONE
            episodesRecyclerView.adapter?.notifyItemInserted(episodes.size)
        }
    }

    fun startAddEpisodeActivity(){
        val intent = Intent(this, AddEpisodeActivity::class.java)
        intent.putExtra("showID", showID)
        startActivity(intent)
    }

}
