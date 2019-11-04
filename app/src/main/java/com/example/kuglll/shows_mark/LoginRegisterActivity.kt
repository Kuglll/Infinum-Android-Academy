package com.example.kuglll.shows_mark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

const val REMEMBERME = "rememberMe"
const val PREFERENCES = "myPreferences"
const val LOGIN_FRAGMENT = "LOGIN_FRAGMENT"

class LoginRegisterActivity : AppCompatActivity() {

    companion object{
        fun startLoginRegisterActivity(context: Context) : Intent{
            return Intent(context, LoginRegisterActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_register_activity)

        if(supportFragmentManager.backStackEntryCount == 0) {
            displayLoginFragment()
        }
    }

    fun displayLoginFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerLoginRegister, LoginFragment.returnLoginFramgent())
            .addToBackStack(LOGIN_FRAGMENT)
            .commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1) {
            finishAffinity()
        } else{
            super.onBackPressed()
        }
    }
}
