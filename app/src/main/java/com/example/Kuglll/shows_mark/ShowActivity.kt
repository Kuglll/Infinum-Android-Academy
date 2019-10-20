package com.example.Kuglll.shows_mark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show.*

class ShowActivity : AppCompatActivity() {

    val shows = mutableListOf<Show>(
        Show(0, "The Big Bang theory", 2007, 2019, R.drawable.theory),
        Show(1, "The Office", 2005, 2013, R.drawable.office),
        Show(2, "House M.D.", 2004, 2012, R.drawable.house),
        Show(3, "Jane The Virgin", 2014, 0, R.drawable.jane),
        Show(4, "Sherlock", 2010, 0, R.drawable.sherlock)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        showsRecyclerView.layoutManager = LinearLayoutManager(this)
        showsRecyclerView.adapter = ShowsAdapter(shows, this)
    }


}
