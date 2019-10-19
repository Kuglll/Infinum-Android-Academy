package com.example.Kuglll.shows_mark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var button_enabled = false
        val mail_regex = Regex("[^@]+@[^\\.]+\\..+")
        val textwatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val username = username_edittext.text
                val password = password_edittext.text

                if(username.length >= 1 && password.length >= 6){
                    login_button.setBackgroundResource(R.drawable.rectangle)
                    button_enabled = true
                } else {
                    login_button.setBackgroundResource(R.drawable.rectangle_light)
                    button_enabled = false
                }
            }
        }

        username_edittext.addTextChangedListener(textwatcher)
        password_edittext.addTextChangedListener(textwatcher)

        login_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if(button_enabled){
                    if (mail_regex.matches(username_edittext.text)){
                        startNewActivity()
                    } else{
                        displayWarning()
                    }
                }
            }
        })

        eye.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                println(password_edittext.inputType)
                if(password_edittext.inputType == InputType.TYPE_CLASS_TEXT) {
                    password_edittext.inputType = 129;
                    password_edittext.setSelection(password_edittext.text.length)
                } else{
                    password_edittext.inputType = InputType.TYPE_CLASS_TEXT
                    password_edittext.setSelection(password_edittext.text.length)
                }
            }
        })


    }

    fun startNewActivity(){
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.putExtra("username", username_edittext.text.toString())
        startActivity(intent)
    }

    fun displayWarning(){
        username_edittext.setBackgroundResource(R.drawable.underline_red)
        username_error_textview.text = "Please enter a valid email address!"
        username_error_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        username_error_textview.setTextColor(resources.getColor(R.color.pink))
    }
}
