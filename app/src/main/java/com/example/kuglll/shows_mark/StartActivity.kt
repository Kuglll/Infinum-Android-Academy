package com.example.kuglll.shows_mark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import kotlinx.android.synthetic.main.activity_start.*


class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        text.visibility = View.INVISIBLE

        Handler().postDelayed(this::startIconAnimation, 100)
        Handler().postDelayed(this::startTextAnimation, 1100)

        Handler().postDelayed(this::startLoginRegisterActivity, 2500)
    }

    fun startIconAnimation(){
        icon.animate()
            .translationY((root.height/2-80).toFloat())
            .setInterpolator(BounceInterpolator())
            .setDuration(1000L)
            .start()
    }

    fun startTextAnimation(){
        text.visibility = View.VISIBLE
        text.animate()
            .scaleXBy(20f)
            .scaleYBy(20f)
            .setInterpolator(OvershootInterpolator())
            .setDuration(1000L)
            .start()
    }

    fun startLoginRegisterActivity(){
        startActivity(LoginRegisterActivity.startLoginRegisterActivity(this))
    }
}
