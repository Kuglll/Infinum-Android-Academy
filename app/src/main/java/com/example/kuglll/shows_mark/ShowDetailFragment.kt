package com.example.kuglll.shows_mark

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_show_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.kuglll.shows_mark.Adapters.EpisodesAdapter
import com.example.kuglll.shows_mark.dataClasses.DataViewModel
import com.example.kuglll.shows_mark.utils.Episode
import com.example.kuglll.shows_mark.utils.EpisodeResult
import com.example.kuglll.shows_mark.utils.ShowDetailResult
import com.example.kuglll.shows_mark.utils.Singleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val SHOWID = "showid"
private const val TITLE = "TITLE"

class ShowDetailFragment : Fragment() {

    var showID = ""
    var showTitle = ""
    var showDescription = ""
    var episodes : MutableList<Episode> = ArrayList()
    lateinit var viewModel: DataViewModel


    companion object{
        fun returnShowDetailFragment(showID: String, title: String) : ShowDetailFragment{
            val args = Bundle()
            args.putString(SHOWID, showID)
            args.putString(TITLE, title)
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
        showID = arguments!!.getString(SHOWID, "")
        showTitle = arguments!!.getString(TITLE, "")
        toolbarTitle.text = showTitle
        fetchShowDetails()
    }

    fun fetchShowDetails(){
        Singleton.createRequest().getShowDetails(showID).enqueue(object : Callback<ShowDetailResult> {
            override fun onFailure(call: Call<ShowDetailResult>, t: Throwable) {
                //TODO: provide title and desription from DB
            }

            override fun onResponse(call: Call<ShowDetailResult>, response: Response<ShowDetailResult>) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        //TODO: update ShowTable with description and likes count
                        showTitle = body.data.title
                        showDescription = body.data.description
                        toolbarTitle.text = showTitle
                        showDescriptionTextView.text = showDescription
                        fetchEpisodes()
                    }
                }
            }

        })
    }

    fun fetchEpisodes(){
        Singleton.createRequest().getShowEpisodes(showID).enqueue(object : Callback<EpisodeResult>{
            override fun onFailure(call: Call<EpisodeResult>, t: Throwable) {
                //TODO: get episodes from DB
            }

            override fun onResponse(call: Call<EpisodeResult>, response: Response<EpisodeResult>) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        body.data.map{ episode ->
                            episodes.add(episode)
                            //TODO: store episodes into database
                        }
                        initEpisodes()
                        initOnClickListeners()
                    }
                }
            }

        })
    }

    fun initEpisodes(){
        episodesRecyclerView.layoutManager = LinearLayoutManager(activity)
        episodesRecyclerView.adapter = EpisodesAdapter(episodes)

        displayEpisodes()
    }

    fun initOnClickListeners(){
        toolbar.setNavigationOnClickListener{activity?.onBackPressed()}

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

