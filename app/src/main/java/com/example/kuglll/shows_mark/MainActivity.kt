package com.example.kuglll.shows_mark

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kuglll.shows_mark.DataClasses.Show

const val SHOW_FRAGMENT = "SHOW_FRAGMENT"

class MainActivity : AppCompatActivity() {

    var userLogedIn = false

    companion object{
        fun startMainActivity(context : Context) : Intent{
            return Intent(context, MainActivity::class.java)
        }
    }

    object storage {
        var drawable : Drawable? = null
        val shows = mutableListOf<Show>(
            Show(0,"The Big Bang theory",2007,2019,R.drawable.theory),
            Show(1, "The Office", 2005, 2013, R.drawable.office,
                "A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium."),
            Show(2,"House M.D.",2004,2012,R.drawable.house),
            Show(3,"Jane The Virgin",2014,0,R.drawable.jane),
            Show(4,"Sherlock",2010,0,R.drawable.sherlock)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        userLogedIn = sharedPref.getBoolean(REMEMBERME, false)

        //only show the fragment when there is no other fragments "started" (upon first start)
        if(supportFragmentManager.backStackEntryCount == 0) {
            displayShowFragment()
        }
    }

    fun displayShowFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ShowFragment.returnShowFragment())
            .addToBackStack(SHOW_FRAGMENT)
            .commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1){
            if(userLogedIn) {
                finishAffinity()
            } else{
                startActivity(LoginRegisterActivity.startLoginRegisterActivity(this))
            }
        }else {
            super.onBackPressed()
        }
    }



}
