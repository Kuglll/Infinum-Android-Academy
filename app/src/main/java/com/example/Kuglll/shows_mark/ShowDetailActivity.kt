package com.example.Kuglll.shows_mark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_show_detail.*
import kotlinx.android.synthetic.main.activity_show_detail.floatingButton
import kotlinx.android.synthetic.main.activity_show_detail_no_episodes.*
import kotlinx.android.synthetic.main.toolbar.*

class ShowDetailActivity : AppCompatActivity() {

    var showID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showID = intent.getIntExtra("showID", -1)
        var episodes = ShowActivity.storage.shows[showID].episodes

        if(episodes.size < 1){
            setContentView(R.layout.activity_show_detail_no_episodes)
            showDescriptionNoEpisodes.text = ShowActivity.storage.shows[showID].description
            floatingButtonNoEpisodes.setOnClickListener(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                    startAddEpisodeActivity()
                }
            })
        }
        else {
            setContentView(R.layout.activity_show_detail)
            showDescription.text = ShowActivity.storage.shows[showID].description
            episodesRecyclerView.layoutManager = LinearLayoutManager(this)
            episodesRecyclerView.adapter = EpisodesAdapter(episodes)
            floatingButton.setOnClickListener(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                    startAddEpisodeActivity()
                }
            })
        }

        val title = intent.getStringExtra("toolbarTitle")
        toolbarTitle.text = title
    }

    fun startAddEpisodeActivity(){
        val intent = Intent(this, AddEpisodeActivity::class.java)
        intent.putExtra("showID", showID)
        startActivity(intent)
    }

}
