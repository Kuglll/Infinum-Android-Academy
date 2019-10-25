package com.example.Kuglll.shows_mark

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
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


        saveButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                ShowActivity.storage.shows[showID].addEpisode(episodeTitleEditText.text.toString())
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        toolbar.setNavigationOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                onBackPressed()
            }
        })

        episodeTitleEditText.doOnTextChanged { text, start, count, after ->  validateInput()}
        episodeDescriptionEditText.doOnTextChanged { text, start, count, after -> validateInput() }

    }

    override fun onBackPressed() {
        if(textInInputFields()){
            displayDialog()
        } else{
            super.onBackPressed()
        }
    }

    fun textInInputFields() : Boolean{
        return episodeTitleEditText.text.isNotEmpty() || episodeDescriptionEditText.text.isNotEmpty()
    }

    fun validateInput() {
        val episodeTitle = episodeTitleEditText.text
        if(episodeTitle.length >= 1){
            saveButton.isEnabled = true
        } else {
            saveButton.isEnabled = false
        }
    }

    fun displayDialog(){
        val builder = AlertDialog.Builder(this@AddEpisodeActivity)
        builder.setTitle("Are you sure you want to quit?")
        builder.setMessage("You left text in input fields.")

        builder.setPositiveButton("YES"){dialog, which ->
            finish()
        }

        builder.setNegativeButton("No"){dialog,which ->
            //I think I can ignore this
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
