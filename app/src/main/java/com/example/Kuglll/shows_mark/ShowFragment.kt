package com.example.Kuglll.shows_mark

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Kuglll.shows_mark.Adapters.ShowsAdapter
import kotlinx.android.synthetic.main.fragment_show.*

class ShowFragment : Fragment() {

    var userLogedIn = false

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
        return inflater.inflate(R.layout.fragment_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        userLogedIn = sharedPref.getBoolean(REMEMBERME, false)

        showsRecyclerView.layoutManager = LinearLayoutManager(activity)
        showsRecyclerView.adapter = ShowsAdapter(MainActivity.storage.shows){ showID -> displayShowDetailFragment(showID) }

        initOnClickListeners()
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

    fun displayShowDetailFragment(showID: Int){
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ShowDetailFragment.returnShowDetailFragment(showID))
            .addToBackStack("ShowDetail")
            .commit()
    }

}
