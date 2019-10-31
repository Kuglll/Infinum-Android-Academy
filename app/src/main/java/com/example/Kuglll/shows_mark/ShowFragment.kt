package com.example.Kuglll.shows_mark

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Kuglll.shows_mark.Adapters.ShowsAdapter
import com.example.Kuglll.shows_mark.DataClasses.Show
import kotlinx.android.synthetic.main.fragment_show.*

class ShowFragment : Fragment() {

    var userLogedIn = false

    companion object{
        fun displayShowFragment(): ShowFragment{
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
        showsRecyclerView.adapter = ShowsAdapter(MainActivity.storage.shows, activity as Activity)

        logoutButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                displayDialog()
            }
        })
    }


    fun displayDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Are you sure you want to log out?")

        builder.setPositiveButton("YES"){dialog, which ->
            logout()
            startActivity(LoginActivity.startLoginActivity(requireContext()))
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

}
