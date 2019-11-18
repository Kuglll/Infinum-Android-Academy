package com.example.kuglll.shows_mark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import kotlinx.android.synthetic.main.activity_start.*


class StartActivity : AppCompatActivity() {

    var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        handler = Handler()

        startTextAnimation()

        handler?.let {
            it.postDelayed(this::startIconAnimation, 100)
            it.postDelayed(this::startLoginRegisterActivity, 2500)
        }
    }

    fun startIconAnimation(){
        icon.animate()
            .translationY((root.height/2-text.height/3).toFloat())
            .setInterpolator(BounceInterpolator())
            .setDuration(1000L)
            .start()
    }

    fun startTextAnimation(){
        text.animate()
            .scaleXBy(1f)
            .scaleYBy(1f)
            .setInterpolator(OvershootInterpolator())
            .setStartDelay(1200L)
            .setDuration(1000L)
            .start()
    }

    fun startLoginRegisterActivity(){
        startActivity(LoginRegisterActivity.startLoginRegisterActivity(this))
    }

    override fun onBackPressed() {
        handler?.removeCallbacksAndMessages(null);
        super.onBackPressed()
    }
}
