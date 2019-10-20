package com.example.Kuglll.shows_mark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.loginButton
import kotlinx.android.synthetic.main.toolbar.*

class AddEpisodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        toolbarTitle.text = "Add episode"

        val textwatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val episodeTitle = episodeTitleEditText.text
                val episodeDescription = episodeDescriptionEditText.text

                if(episodeTitle.length >= 1 && episodeDescription.length >= 1){
                    loginButton.isEnabled = true
                } else {
                    loginButton.isEnabled = false
                }
            }
        }

        saveButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

            }
        })

        usernameEditText.addTextChangedListener(textwatcher)
        passwordEdittext.addTextChangedListener(textwatcher)


    }
}
