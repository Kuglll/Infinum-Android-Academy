package com.example.kuglll.shows_mark

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.kuglll.shows_mark.dataClasses.DataViewModel
import com.example.kuglll.shows_mark.databinding.FragmentEpisodeDetailBinding
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_episode_detail.*
import kotlinx.android.synthetic.main.toolbar.*

private const val EPISODEID = "EPISODEID"

class EpisodeDetailFragment: Fragment(), FragmentBackListener{

    var episodeId = ""
    lateinit var viewModel: DataViewModel
    var loadingDialog: AlertDialog? = null


    companion object{
        fun returnEpisodeDetailFragment(episodeId: String): EpisodeDetailFragment{
            val args = Bundle()
            args.putString(EPISODEID, episodeId)
            val fragment = EpisodeDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(DataViewModel::class.java)
        }
        val binding: FragmentEpisodeDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_episode_detail, container, false)
        binding.viewmodel = viewModel
        binding.setLifecycleOwner { lifecycle }
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.episodeTitle.value = "Missing title!"
        viewModel.episodeDescription.value = "Missing description!"
        viewModel.episodeSeasonNumber.value = "S01 E01"

        arguments?.let {
            episodeId = it.getString(EPISODEID, "")
        }

        viewModel.getEpisodeDetails(episodeId, {startDialog()}, {requestFailed -> stopDialog(requestFailed)})

        initOnClickListeners()
        initObservers()
    }

    fun startDialog(){
        loadingDialog = SpotsDialog.Builder().setContext(context).build()
        loadingDialog?.let {
            it.show()
        }
    }

    fun stopDialog(requestFailed: Boolean){
        if(requestFailed){
            Toast.makeText(context, "To show episode details, you need internet connection!", Toast.LENGTH_LONG).show()
        }
        if(loadingDialog != null){
            loadingDialog!!.dismiss()
            loadingDialog = null
        }
    }

    fun initOnClickListeners(){
        toolbar.setNavigationOnClickListener{activity?.onBackPressed()}

        commentsGroup.setAllOnClickListeners(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                displayCommentsFragment()
            }
        })
    }

    fun Group.setAllOnClickListeners(listener: View.OnClickListener){
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnClickListener(listener)
        }
    }

    fun displayCommentsFragment(){
        activity?.let {
            it.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CommentsFragment.returnCommentsFragment(episodeId))
                .addToBackStack("Comments")
                .commit()
        }
    }


    fun initObservers(){
        viewModel.episodeImage.observe(this, Observer<String> { imageUrl ->
            loadImage(imageUrl)
        })
    }

    fun loadImage(imageUrl: String){
        if(imageUrl != "") {
            Picasso.get().load("https://api.infinum.academy${imageUrl}")
                .into(episodeImage)
        }
    }

    override fun onPause() {
        super.onPause()
        if(loadingDialog != null){
            loadingDialog!!.dismiss()
            loadingDialog = null
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}