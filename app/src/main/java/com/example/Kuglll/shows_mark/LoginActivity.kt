package com.example.Kuglll.shows_mark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_login.*

const val REMEMBERME = "rememberMe"
const val PREFERENCES = "myPreferences"

class LoginActivity : AppCompatActivity() {

    var userLogedIn = false
    val mail_regex = Regex("[^@]+@[^\\.]+\\..+")

    companion object{
        fun startLoginActivity(context: Context) : Intent{
            val intent = Intent(context, LoginActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPref = this.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        userLogedIn = sharedPref.getBoolean(REMEMBERME, false)


        if(userLogedIn) startActivity(MainActivity.startMainActivity(this@LoginActivity))


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
            val sharedPref = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putBoolean(REMEMBERME, true)
                apply()
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
