package com.example.Kuglll.shows_mark

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome.*
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


private const val USERNAME = "username"

class WelcomeActivity : AppCompatActivity() {

    companion object {
        fun startWelcomeActivity(context: Context, username : String): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(USERNAME, username)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        var username = intent.getStringExtra("username").split("@")[0]
        welcomeUserTextview.text = "Welcome $username"

        Handler().postDelayed(this::startMainActivity, 2000)
    }

    fun startMainActivity(){
        startActivity(MainActivity.startMainActivity(this@WelcomeActivity))
    }
}
