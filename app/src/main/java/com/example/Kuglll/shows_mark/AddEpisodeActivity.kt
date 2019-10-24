package com.example.Kuglll.shows_mark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.toolbar.*

private const val SHOWID = "showid"

class AddEpisodeActivity : AppCompatActivity() {

    companion object {
        fun startAddEpisodeActvity(context : Context, showID : Int): Intent {
            val intent = Intent(context, AddEpisodeActivity::class.java)
            intent.putExtra(SHOWID, showID)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        val showID = intent.getIntExtra(SHOWID, -1)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbarTitle.text = "Add episode"

        val textwatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val episodeTitle = episodeTitleEditText.text

                if(episodeTitle.length >= 1){
                    saveButton.isEnabled = true
                } else {
                    saveButton.isEnabled = false
                }
            }
        }

        saveButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                ShowActivity.storage.shows[showID].addEpisode(episodeTitleEditText.text.toString())
                finish()
            }
        })

        toolbar.setNavigationOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                //check if there is something in edittext + dialog
                onBackPressed()
            }
        })

        episodeTitleEditText.addTextChangedListener(textwatcher)
        episodeDescriptionEditText.addTextChangedListener(textwatcher)


    }
}
