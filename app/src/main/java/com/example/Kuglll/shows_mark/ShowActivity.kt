package com.example.Kuglll.shows_mark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show.*

class ShowActivity : AppCompatActivity() {

    var userLogedIn = false
    var userWantsToLogout = false

    object storage {
        val shows = mutableListOf<Show>(
            Show(0, "The Big Bang theory", 2007, 2019, R.drawable.theory),
            Show(1, "The Office", 2005, 2013, R.drawable.office,
                "A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium."),
            Show(2, "House M.D.", 2004, 2012, R.drawable.house),
            Show(3, "Jane The Virgin", 2014, 0, R.drawable.jane),
            Show(4, "Sherlock", 2010, 0, R.drawable.sherlock)
        )
    }

    companion object{
        fun startShowActivity(context : Context) : Intent {
            val intent = Intent(context, ShowActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        val sharedPref = this?.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        userLogedIn = sharedPref.getBoolean(REMEMBERME, false)

        showsRecyclerView.layoutManager = LinearLayoutManager(this)
        showsRecyclerView.adapter = ShowsAdapter(storage.shows, this)

        logoutButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                displayDialog()
            }
        })
    }

    fun displayDialog(){
        val builder = AlertDialog.Builder(this@ShowActivity)
        builder.setTitle("Are you sure you want to log out?")

        builder.setPositiveButton("YES"){dialog, which ->
            logout()
            startActivity(LoginActivity.startLoginActivity(this@ShowActivity))
        }

        builder.setNegativeButton("No"){dialog,which ->
            //I think I can ignore this
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun logout(){
        val sharedPref = this?.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(REMEMBERME, false)
            commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(userLogedIn) {
            finishAffinity()
        }
        else {
            startActivity(LoginActivity.startLoginActivity(this@ShowActivity))
        }
    }

}
