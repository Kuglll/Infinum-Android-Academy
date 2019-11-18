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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
    var token: String? = null
    var episodes : List<Episode> = ArrayList()
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
        token = sharedPref.getString(TOKEN, null)

        viewModel.showDescription.value = "Missing description!"
        viewModel.likeStatus.value = null
        viewModel.episodes.postValue(listOf<Episode>())


        showID = arguments!!.getString(SHOWID, "")
        showTitle = arguments!!.getString(TITLE, "")
        toolbarTitle.text = showTitle

        viewModel.fetchShowDetails(showID)
        viewModel.fetchEpisodes(showID)
        checkForLikeStatus(showID)

        initOnClickListeners()
        initObservers()
    }

    fun checkForLikeStatus(showId: String){
        viewModel.checkDatabaseForLikeStatus(showId)
    }


    fun initObservers(){
        viewModel.likeStatus.observe(this, Observer<Boolean?> { likeStatus ->
            updateLikeButtons(likeStatus)
        })
        viewModel.toolbarTitle.observe(this, Observer<String> { showTitle ->
            toolbarTitle.text = showTitle
        })
        viewModel.showDescription.observe(this, Observer<String> { showDescription ->
            showDescriptionTextView.text = showDescription
        })
        viewModel.likesCount.observe(this, Observer<Int>{lCount ->
            likesCount.text = lCount.toString()
        })
        viewModel.episodes.observe(this, Observer<List<Episode>> {
            episodes = it
            initEpisodes()
        })
    }

    fun updateLikeButtons(likeStatus: Boolean?){
        if(likeStatus == true){
            like.isEnabled = false
            dislike.isEnabled = true
            like.setBackgroundResource(R.drawable.ic_like_pressed)
            dislike.setBackgroundResource(R.drawable.ic_dislike)
        }
        else if(likeStatus == false){
            like.isEnabled = true
            dislike.isEnabled = false
            like.setBackgroundResource(R.drawable.ic_like)
            dislike.setBackgroundResource(R.drawable.ic_dislike_pressed)
        }
    }

    fun initOnClickListeners(){
        toolbar.setNavigationOnClickListener{activity?.onBackPressed()}

        floatingButton.setOnClickListener { displayAddEpisodeFragment() }

        like.setOnClickListener{ viewModel.likeShow(showID, token) }

        dislike.setOnClickListener{ viewModel.dislikeShow(showID, token) }

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
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AddEpisodeFragment.returnAddEpisodeFragment(showID))
            .addToBackStack("AddEpisode")
            .commit()
    }

    fun initEpisodes(){
        episodesRecyclerView.layoutManager = LinearLayoutManager(activity)
        episodesRecyclerView.adapter = EpisodesAdapter(episodes)

        displayEpisodes()
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

    override fun onBackPressed(): Boolean {
        return false
    }

}

