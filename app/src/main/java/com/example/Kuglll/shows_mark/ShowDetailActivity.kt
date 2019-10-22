package com.example.Kuglll.shows_mark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show_detail.*
import kotlinx.android.synthetic.main.toolbar.*

private const val SHOWNAME = "showname"
private const val SHOWID = "showid"

class ShowDetailActivity : AppCompatActivity() {

    var showID = 0
    //there must be a better way of initializing empty list
    var episodes : MutableList<String> = ArrayList()

    companion object{
        fun startShowDetailActivity(context : Context, showname : String, showID : Int) : Intent{
            val intent = Intent(context, ShowDetailActivity::class.java)
            intent.putExtra(SHOWNAME, showname)
            intent.putExtra(SHOWID, showID)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val title = intent.getStringExtra(SHOWNAME)
        toolbarTitle.text = title

        showID = intent.getIntExtra(SHOWID, -1)
        episodes = ShowActivity.storage.shows[showID].episodes
        showDescription.text = ShowActivity.storage.shows[showID].description

        floatingButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                startActivity(AddEpisodeActivity.startAddEpisodeActvity(this@ShowDetailActivity, showID))
            }
        })

        toolbar.setNavigationOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                finish()
            }
        })

        episodesRecyclerView.layoutManager = LinearLayoutManager(this)
        episodesRecyclerView.adapter = EpisodesAdapter(episodes)

    }

    override fun onResume() {
        super.onResume()

        if(episodes.size > 0){
            episodesRecyclerView.visibility = View.VISIBLE
            iconSleep.visibility = View.GONE
            fallenAsleepTextView.visibility = View.GONE
            fallenAsleepTextView2.visibility = View.GONE
            episodesRecyclerView.adapter?.notifyItemInserted(episodes.size)
        }
    }

}
