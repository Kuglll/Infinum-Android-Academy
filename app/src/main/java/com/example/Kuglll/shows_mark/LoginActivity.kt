package com.example.Kuglll.shows_mark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
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
                val username = usernameEditText.text
                val password = passwordEdittext.text

                if(username.length >= 1 && password.length >= 6){
                    loginButton.setBackgroundResource(R.drawable.rectangle)
                    button_enabled = true
                } else {
                    loginButton.setBackgroundResource(R.drawable.rectangle_light)
                    button_enabled = false
                }
            }
        }

        usernameEditText.addTextChangedListener(textwatcher)
        passwordEdittext.addTextChangedListener(textwatcher)

        loginButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if(button_enabled){
                    if (mail_regex.matches(usernameEditText.text)){
                        startNewActivity()
                    } else{
                        displayWarning()
                    }
                }
            }
        })

        eye.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                println(passwordEdittext.inputType)
                if(passwordEdittext.inputType == InputType.TYPE_CLASS_TEXT) {
                    passwordEdittext.inputType = 129;
                    passwordEdittext.setSelection(passwordEdittext.text.length)
                } else{
                    passwordEdittext.inputType = InputType.TYPE_CLASS_TEXT
                    passwordEdittext.setSelection(passwordEdittext.text.length)
                }
            }
        })

    }

    fun startNewActivity(){
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.putExtra("username", usernameEditText.text.toString())
        startActivity(intent)
    }

    fun displayWarning(){
        usernameEditText.setBackgroundResource(R.drawable.underline_red)
        usernameErrorTextview.text = "Please enter a valid email address!"
        usernameErrorTextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        usernameErrorTextview.setTextColor(resources.getColor(R.color.pink))
    }
}
