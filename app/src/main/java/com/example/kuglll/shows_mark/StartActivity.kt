package com.example.kuglll.shows_mark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        Handler().postDelayed(this::startLoginRegisterActivity, 2000)
    }

    fun startLoginRegisterActivity(){
        startActivity(LoginRegisterActivity.startLoginRegisterActivity(this))
    }
}
