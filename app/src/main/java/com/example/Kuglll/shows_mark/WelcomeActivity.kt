package com.example.Kuglll.shows_mark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome.*
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler


class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        var username : String? = intent.getStringExtra("username").split("@")[0]
        welcomeUserTextview.text = "Welcome $username"

        Handler().postDelayed(this::startShowActivity, 2000)
    }

    fun startShowActivity(){
        val intent = Intent(this, ShowActivity::class.java)
        startActivity(intent)
    }

}
