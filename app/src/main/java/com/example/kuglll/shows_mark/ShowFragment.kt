package com.example.kuglll.shows_mark

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuglll.shows_mark.Adapters.ShowsAdapter
import com.example.kuglll.shows_mark.dataClasses.DataViewModel
import com.example.kuglll.shows_mark.database.Repository
import com.example.kuglll.shows_mark.database.ShowTable
import com.example.kuglll.shows_mark.utils.Show
import com.example.kuglll.shows_mark.utils.ShowResult
import com.example.kuglll.shows_mark.utils.Singleton
import kotlinx.android.synthetic.main.fragment_show.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowFragment : Fragment(), FragmentBackListener {

    var userLogedIn = false
    var shows = ArrayList<Show>()
    lateinit var call: Call<ShowResult>
    lateinit var viewModel: DataViewModel

    companion object{
        fun returnShowFragment(): ShowFragment{
            return ShowFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(requireActivity()).get(DataViewModel::class.java)
        return inflater.inflate(R.layout.fragment_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        userLogedIn = sharedPref.getBoolean(REMEMBERME, false)

        initShowObserver()

        shows = ArrayList()

        viewModel.loadShows()

        initOnClickListeners()
    }

    fun initShowObserver(){
        viewModel.shows.observe(this, Observer<List<Show>> { shows ->
            updateUi(shows)
        })
    }

    fun updateUi(shows: List<Show>){
        if(shows.isEmpty()) {
            showsRecyclerView.visibility = View.GONE
            sleepGroupShows.visibility = View.VISIBLE
        } else {
            showsRecyclerView.visibility = View.VISIBLE
            sleepGroupShows.visibility = View.GONE
        }
        showsRecyclerView.layoutManager = LinearLayoutManager(activity)
        showsRecyclerView.adapter =
            ShowsAdapter(shows) { showID, title -> displayShowDetailFragment(showID, title) }

    }

    fun fetchShows(){
        call = Singleton.service.getShows()
        call.enqueue(object: Callback<ShowResult>{
            override fun onFailure(call: Call<ShowResult>, t: Throwable) {
                if(!call.isCanceled) getDataFromDatabase()
            }

            override fun onResponse(call: Call<ShowResult>, response: Response<ShowResult>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        body.data.map {Show ->
                            shows.add(Show)
                            Repository.getShowById(Show.id){
                                if (it == null) addShowtoDatabase(Show)
                            }
                        }
                        initRecyclerview()
                    }
                }
            }
        })
    }

    fun addShowtoDatabase(show: Show){
        Repository.addShow(ShowTable(
            show.id,
            show.title,
            show.imageUrl,
            show.likesCount
        ))
    }

    fun getDataFromDatabase(){
        Repository.getShows{
            it.map {show ->
                shows.add(Show(
                    show.id,
                    show.title,
                    show.imageUrl,
                    show.likesCount
                ))
            }

            activity?.runOnUiThread {
                initRecyclerview()
            }
        }
    }

    fun initRecyclerview(){
        if(shows.size > 0) {
            showsRecyclerView.layoutManager = LinearLayoutManager(activity)
            showsRecyclerView.adapter =
                ShowsAdapter(shows) { showID, title -> displayShowDetailFragment(showID, title) }
            showsRecyclerView.visibility = View.VISIBLE
            sleepGroupShows.visibility = View.GONE
        }
    }

    fun initOnClickListeners(){
        logoutButton.setOnClickListener {
            if(userLogedIn) {
                displayDialog()
            } else{
                startActivity(LoginRegisterActivity.startLoginRegisterActivity(requireContext()))
                activity!!.finish()
             }
        }
    }


    fun displayDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Are you sure you want to log out?")

        builder.setPositiveButton("YES"){dialog, which ->
            logout()
            startActivity(LoginRegisterActivity.startLoginRegisterActivity(requireContext()))
            activity!!.finish()
        }

        builder.setNegativeButton("No"){dialog,which ->
            //I think I can ignore this
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun logout(){
        val sharedPref = requireActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(REMEMBERME, false)
            commit()
        }
    }

    fun displayShowDetailFragment(showID: String, title: String){
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ShowDetailFragment.returnShowDetailFragment(showID, title))
            .addToBackStack("ShowDetail")
            .commit()
    }

    override fun onBackPressed(): Boolean{
        call.cancel()
        if(userLogedIn){
            requireActivity().finishAffinity()
        } else {
            startActivity(LoginRegisterActivity.startLoginRegisterActivity(requireActivity()))
        }
        return true
    }

}
