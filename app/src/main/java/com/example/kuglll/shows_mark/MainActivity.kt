package com.example.kuglll.shows_mark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

const val SHOW_FRAGMENT = "SHOW_FRAGMENT"

class MainActivity : AppCompatActivity() {

    var userLogedIn = false

    companion object{
        fun startMainActivity(context : Context) : Intent{
            return Intent(context, MainActivity::class.java)
        }
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
