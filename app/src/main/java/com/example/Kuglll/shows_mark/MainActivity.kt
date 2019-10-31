package com.example.Kuglll.shows_mark

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.Kuglll.shows_mark.DataClasses.Show

class MainActivity : AppCompatActivity() {

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

        startShowFragment()
    }

    fun startShowFragment(){
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, ShowFragment.displayShowFragment())
            .addToBackStack("Show")
            .commit()
    }





}
