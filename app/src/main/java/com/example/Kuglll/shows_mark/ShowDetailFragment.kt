package com.example.Kuglll.shows_mark

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_show_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.Kuglll.shows_mark.Adapters.EpisodesAdapter
import com.example.Kuglll.shows_mark.DataClasses.DataViewModel
import com.example.Kuglll.shows_mark.DataClasses.Episode

private const val SHOWID = "showid"

class ShowDetailFragment : Fragment() {

    var showID = 0
    var episodes : MutableList<Episode> = ArrayList()
    lateinit var viewModel: DataViewModel


    companion object{
        fun returnShowDetailFragment(showID : Int) : ShowDetailFragment{
            val args = Bundle()
            args.putInt(SHOWID, showID)
            val fragment = ShowDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        showID = arguments!!.getInt(SHOWID, -1)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbarTitle.text = MainActivity.storage.shows[showID].name

        episodes = MainActivity.storage.shows[showID].episodes
        showDescription.text = MainActivity.storage.shows[showID].description

        toolbar.setNavigationOnClickListener{activity?.onBackPressed()}

        displayEpisodes()

        initOnClickListeners()

        episodesRecyclerView.layoutManager = LinearLayoutManager(activity)
        episodesRecyclerView.adapter = EpisodesAdapter(episodes)
    }

    fun initOnClickListeners(){
        floatingButton.setOnClickListener { displayAddEpisodeFragment() }

        sleepGroup.setAllOnClickListeners(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                displayAddEpisodeFragment()
            }
        })
    }

    fun Group.setAllOnClickListeners(listener: View.OnClickListener){
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnClickListener(listener)
        }
    }

    fun displayAddEpisodeFragment(){
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AddEpisodeFragment.returnAddEpisodeFragment(showID))
            .addToBackStack("AddEpisode")
            .commit()
    }

    fun displayEpisodes() {
        checkForNewEpisode()
        if(episodes.size>0){
            episodesRecyclerView.visibility = View.VISIBLE
            sleepGroup.visibility = View.GONE
        }else {
            episodesRecyclerView.visibility = View.GONE
            sleepGroup.visibility = View.VISIBLE
        }
    }


    fun checkForNewEpisode(){
        if(viewModel.episodeInserted.value == true){
            viewModel.episodeInserted.value = false
            episodesRecyclerView.adapter?.notifyItemInserted(episodes.size)
        }
    }


}

