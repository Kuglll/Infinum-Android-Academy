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
import android.app.Activity
import androidx.constraintlayout.widget.Group


private const val REQUESTCODE = 1
private const val SHOWID = "showid"

class ShowDetailActivity : AppCompatActivity() {

    var showID = 0
    //there must be a better way of initializing empty list
    var episodes : MutableList<Episode> = ArrayList()

    companion object{
        fun startShowDetailActivity(context : Context, showID : Int) : Intent{
            val intent = Intent(context, ShowDetailActivity::class.java)
            intent.putExtra(SHOWID, showID)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)

        showID = intent.getIntExtra(SHOWID, -1)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbarTitle.text = ShowActivity.storage.shows[showID].name

        episodes = ShowActivity.storage.shows[showID].episodes
        showDescription.text = ShowActivity.storage.shows[showID].description

        toolbar.setNavigationOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                onBackPressed()
            }
        })

        initOnClickListeners()

        episodesRecyclerView.layoutManager = LinearLayoutManager(this)
        episodesRecyclerView.adapter = EpisodesAdapter(episodes)

    }

    fun initOnClickListeners(){
        floatingButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                startActivityForResult(AddEpisodeActivity.startAddEpisodeActvity(this@ShowDetailActivity, showID), REQUESTCODE)
            }
        })

        sleepGroup.setAllOnClickListeners(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                startActivityForResult(AddEpisodeActivity.startAddEpisodeActvity(this@ShowDetailActivity, showID), REQUESTCODE)
            }
        })
    }

    fun Group.setAllOnClickListeners(listener: View.OnClickListener){
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnClickListener(listener)
        }
    }

    override fun onResume() {
        super.onResume()
        if(episodes.size>0){
            episodesRecyclerView.visibility = View.VISIBLE
            sleepGroup.visibility = View.GONE
        }else {
            episodesRecyclerView.visibility = View.GONE
            sleepGroup.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUESTCODE) {
            if (resultCode == Activity.RESULT_OK) {
                episodesRecyclerView.adapter?.notifyItemInserted(episodes.size)
            }
        }

    }

}
