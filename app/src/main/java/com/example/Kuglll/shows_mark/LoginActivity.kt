package com.example.Kuglll.shows_mark

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_login.*

const val REMEMBERME = "rememberMe"

class LoginActivity : AppCompatActivity() {

    var userLogedIn = false
    val mail_regex = Regex("[^@]+@[^\\.]+\\..+")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //get userLogedIn from sharedPreferences
        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
        userLogedIn = sharedPref.getBoolean(REMEMBERME, false)


        if(userLogedIn) startActivity(ShowActivity.startShowActivity(this@LoginActivity))


        usernameEditText.doOnTextChanged { text, start, count, after ->  validateInput()}
        passwordEdittext.doOnTextChanged { text, start, count, after ->  validateInput()}

        loginButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (mail_regex.matches(usernameEditText.text)){
                    checkForRememberMe()
                    startActivity(WelcomeActivity.startWelcomeActivity(this@LoginActivity, usernameEditText.text.toString()))
                } else{
                    displayWarning()
                }
            }
        })
    }

    fun validateInput(){
        val username = usernameEditText.text
        val password = passwordEdittext.text

        if(username.length >= 1 && password.length >= 6){
            loginButton.isEnabled = true
        } else {
            loginButton.isEnabled = false
        }
    }

    fun checkForRememberMe(){
        if (rememberMeCheckBox.isChecked){
            val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putBoolean(REMEMBERME, true)
                commit()
            }
        }
    }

    fun displayWarning(){
        usernameEditText.setBackgroundResource(R.drawable.underline_red)
        usernameErrorTextview.text = "Please enter a valid email address!"
        usernameErrorTextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        usernameErrorTextview.setTextColor(resources.getColor(R.color.pink))
    }
}
