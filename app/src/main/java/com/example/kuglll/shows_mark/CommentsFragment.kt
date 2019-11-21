package com.example.kuglll.shows_mark

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuglll.shows_mark.Adapters.CommentsAdapter
import com.example.kuglll.shows_mark.dataClasses.DataViewModel
import com.example.kuglll.shows_mark.databinding.FragmentCommentsBinding
import com.example.kuglll.shows_mark.utils.Comment
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.toolbar.*

private const val EPISODEID = "EPISODEDI"

class CommentsFragment: Fragment(), FragmentBackListener{

    var episodeId = ""
    lateinit var viewModel: DataViewModel
    var loadingDialog: AlertDialog? = null
    var token: String? = null

    companion object{
        fun returnCommentsFragment(episodeId: String): CommentsFragment{
            val args = Bundle()
            args.putString(EPISODEID, episodeId)
            val fragment = CommentsFragment()
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
        val binding: FragmentCommentsBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false)
        binding.viewmodel = viewModel
        binding.setLifecycleOwner { lifecycle }
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            val sharedPref = it.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            token = sharedPref.getString(TOKEN, null)
        }

        viewModel.comments.postValue(listOf())

        arguments?.let {
            episodeId = it.getString(EPISODEID, "")
        }

        viewModel.getEpisodeComments(episodeId, {startDialog()}, {requestFailed -> stopDialog(requestFailed)})

        initOnClickListeners()
        initObservers()
    }

    fun initOnClickListeners(){
        toolbar.setNavigationOnClickListener{activity?.onBackPressed()}

        postComment.setOnClickListener {
            postComment(commentEdittext.text.toString())
            commentEdittext.setText("")
        }
    }

    fun postComment(message: String){
        viewModel.uploadComment(message, episodeId, token, {startDialog()}, {requestFailed -> stopDialogAndFetchComments(requestFailed)})
    }

    fun initObservers(){
        viewModel.comments.observe(this, Observer<List<Comment>>{ comments ->
            updateUi(comments)
        })
    }

    fun stopDialogAndFetchComments(requestFailed: Boolean){
        stopDialog(requestFailed)
        viewModel.getEpisodeComments(episodeId, {startDialog()}, {request -> stopDialog(request)})
    }

    fun updateUi(comments: List<Comment>){
        if(comments.size > 0){
            toolbarTitle.text = "Comments"
            commentsRecyclerView.layoutManager = LinearLayoutManager(activity)
            commentsRecyclerView.adapter = CommentsAdapter(comments)

            commentsRecyclerView.visibility = View.VISIBLE
            noCommentsGroup.visibility = View.GONE

        } else{
            toolbarTitle.text = ""
            commentsRecyclerView.visibility = View.GONE
            noCommentsGroup.visibility = View.VISIBLE
        }
    }

    fun startDialog(){
        loadingDialog = SpotsDialog.Builder().setContext(context).build()
        loadingDialog?.let {
            it.show()
        }
    }

    fun stopDialog(requestFailed: Boolean){
        if(requestFailed){
            Toast.makeText(context, "To show comments, you need internet connection!", Toast.LENGTH_LONG).show()
        }
        if(loadingDialog != null){
            loadingDialog!!.dismiss()
            loadingDialog = null
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