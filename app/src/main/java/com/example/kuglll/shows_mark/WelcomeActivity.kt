package com.example.kuglll.shows_mark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome.*
import android.os.Handler


private const val USERNAME = "username"

class WelcomeActivity : AppCompatActivity() {

    var handler: Handler? = null

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

        handler = Handler()
        handler?.let {
            it.postDelayed(this::startMainActivity, 2000)
        }
    }

    fun startMainActivity(){
        startActivity(MainActivity.startMainActivity(this@WelcomeActivity))
        finish()
    }

    override fun onPause() {
        handler?.removeCallbacksAndMessages(null)
        onBackPressed()
        super.onPause()
    }

    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }
}
