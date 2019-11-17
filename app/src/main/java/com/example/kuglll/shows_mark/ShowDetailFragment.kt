package com.example.kuglll.shows_mark

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_show_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.kuglll.shows_mark.Adapters.EpisodesAdapter
import com.example.kuglll.shows_mark.dataClasses.DataViewModel
import com.example.kuglll.shows_mark.database.EpisodeTable
import com.example.kuglll.shows_mark.database.Repository
import com.example.kuglll.shows_mark.databinding.FragmentShowDetailBinding
import com.example.kuglll.shows_mark.utils.Episode
import com.example.kuglll.shows_mark.utils.EpisodeResult
import com.example.kuglll.shows_mark.utils.ShowDetailResult
import com.example.kuglll.shows_mark.utils.Singleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val SHOWID = "showid"
private const val TITLE = "TITLE"

class ShowDetailFragment : Fragment(), FragmentBackListener {

    var showID = ""
    var showTitle = ""
    var token: String? = ""
    var episodes : MutableList<Episode> = ArrayList()
    lateinit var viewModel: DataViewModel
    var callShowDetail: Call<ShowDetailResult>? = null
    var callEpisodeResult: Call<EpisodeResult>? = null


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
        viewModel = ViewModelProviders.of(requireActivity()).get(DataViewModel::class.java)
        val binding: FragmentShowDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_detail, container, false)
        binding.viewmodel = viewModel
        binding.setLifecycleOwner { lifecycle }
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        token = sharedPref.getString(TOKEN, "")

        viewModel.showDescription.value = "Missing description!"
        showID = arguments!!.getString(SHOWID, "")
        showTitle = arguments!!.getString(TITLE, "")
        toolbarTitle.text = showTitle
        fetchShowDetails()
        initOnClickListeners()
    }

    fun fetchShowDetails(){
        callShowDetail = Singleton.service.getShowDetails(showID)
        callShowDetail?.enqueue(object : Callback<ShowDetailResult> {

            override fun onFailure(call: Call<ShowDetailResult>, t: Throwable) {
                if(!call.isCanceled) {
                    Repository.getShowDetailsById(showID) { description, likesCount ->
                        viewModel.showDescription.postValue(description)
                        viewModel.likesCount.postValue(likesCount)
                    }
                    getEpisodesFromDatabase()
                }
            }

            override fun onResponse(call: Call<ShowDetailResult>, response: Response<ShowDetailResult>) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        Repository.updateDesctiption(body.data.description, body.data.id)
                        showTitle = body.data.title
                        toolbarTitle.text = showTitle
                        viewModel.showDescription.value = body.data.description
                        viewModel.likesCount.value = body.data.likesCount
                        fetchEpisodes()
                    }
                }
            }

        })
    }

    fun fetchEpisodes(){
        callEpisodeResult = Singleton.service.getShowEpisodes(showID)
        callEpisodeResult?.enqueue(object : Callback<EpisodeResult>{
            override fun onFailure(call: Call<EpisodeResult>, t: Throwable) {
            }

            override fun onResponse(call: Call<EpisodeResult>, response: Response<EpisodeResult>) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        body.data.map{ episode ->
                            episodes.add(episode)
                            Repository.getEpisodeById(episode.id){
                                if (it == null) addEpisodeToDatabase(episode)
                            }
                        }
                        initEpisodes()
                    }
                }
            }

        })
    }

    fun getEpisodesFromDatabase(){
        Repository.getEpisodesByShowId(showID){
            it.map{episode ->
                episodes.add(
                    Episode(
                        episode.id,
                        episode.title,
                        episode.description,
                        episode.imageUrl,
                        episode.episodeNumber,
                        episode.seasonNumber
                    )
                )
            }

            activity?.runOnUiThread {
                initEpisodes()
                initOnClickListeners()
            }
        }
    }

    fun addEpisodeToDatabase(episode: Episode){
        Repository.addEpisode(EpisodeTable(
            showID,
            episode.id,
            episode.title,
            episode.description,
            episode.imageUrl,
            episode.episodeNumber,
            episode.seasonNumber
        ))
    }

    fun initEpisodes(){
        episodesRecyclerView.layoutManager = LinearLayoutManager(activity)
        episodesRecyclerView.adapter = EpisodesAdapter(episodes)

        displayEpisodes()
    }

    fun initOnClickListeners(){
        toolbar.setNavigationOnClickListener{activity?.onBackPressed()}

        floatingButton.setOnClickListener { displayAddEpisodeFragment() }

        sleepGroupEpisodes.setAllOnClickListeners(object : View.OnClickListener{
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
        cancleAllApiCalls()
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AddEpisodeFragment.returnAddEpisodeFragment(showID))
            .addToBackStack("AddEpisode")
            .commit()
    }

    fun displayEpisodes() {
        checkForNewEpisode()
        if(episodes.size>0){
            episodesRecyclerView.visibility = View.VISIBLE
            sleepGroupEpisodes.visibility = View.GONE
        }else {
            episodesRecyclerView.visibility = View.GONE
            sleepGroupEpisodes.visibility = View.VISIBLE
        }
    }


    fun checkForNewEpisode(){
        if(viewModel.episodeInserted.value == true){
            viewModel.episodeInserted.value = false
            episodesRecyclerView.adapter?.notifyItemInserted(episodes.size)
        }
    }

    fun cancleAllApiCalls(){
        if(callEpisodeResult != null) callEpisodeResult?.cancel()
        if(callShowDetail != null) callShowDetail?.cancel()
    }

    override fun onBackPressed(): Boolean {
        cancleAllApiCalls()
        return false
    }

}

