package com.example.Kuglll.shows_mark

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.Kuglll.shows_mark.Utils.RegisterRequest
import com.example.Kuglll.shows_mark.Utils.RegisterResult
import com.example.Kuglll.shows_mark.Utils.Singleton
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val USERNAME_REGISTER = "USERNAME"
const val PASSWORD_REGISTER = "PASSWORD"

class RegisterFragment : Fragment(){

    val mail_regex = Regex("[^@]+@[^\\.]+\\..+")
    var userRegistered = false

    companion object {
        fun returnRegisterFragment(username: String = "", password: String = ""): Fragment {
            val args = Bundle()
            args.putString(USERNAME_REGISTER, username)
            args.putString(PASSWORD_REGISTER, password)
            val fragment = RegisterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarTitle.text = "Register"

        checkForArguments()
        initOnClickListeners()
    }

    fun checkForArguments(){
        val username = arguments!!.getString(USERNAME_REGISTER, "").toString()
        val password = arguments!!.getString(PASSWORD_REGISTER, "").toString()
        emailEditText.setText(username)
        registerPasswordEdittext.setText(password)
        registerAgainPasswordEdittext.setText(password)
        validateInput()
    }

    fun initOnClickListeners(){
        emailEditText.doOnTextChanged { text, start, count, after ->  validateInput()}
        registerPasswordEdittext.doOnTextChanged { text, start, count, after ->  validateInput()}
        registerAgainPasswordEdittext.doOnTextChanged { text, start, count, after ->  validateInput()}

        toolbar.setNavigationOnClickListener{ activity!!.onBackPressed() }

        registerButton.setOnClickListener{
            if(passwordsMatch() && emailMatchesRegex(emailEditText.text.toString())){
                registerUser(emailEditText.text.toString(), registerPasswordEdittext.text.toString())
                if(userRegistered){
                    startActivity(WelcomeActivity.startWelcomeActivity(activity!!, emailEditText.text.toString()))
                }
            } else{
                displayWarning()
            }
        }
    }

    fun registerUser(email : String, password: String){
        Singleton.createRequest().Register(RegisterRequest(email, password)).enqueue(object : Callback<RegisterResult>{
            override fun onFailure(call: Call<RegisterResult>, t: Throwable) {

            }

            override fun onResponse(call: Call<RegisterResult>, response: Response<RegisterResult>){
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        userRegistered = true
                    } else{
                        println("Body is null")
                    }
                } else println("Something went wrong!")
            }
        })

    }

    fun passwordsMatch(): Boolean{
        return registerPasswordEdittext.text.toString() == registerAgainPasswordEdittext.text.toString()
    }

    fun emailMatchesRegex(email: String): Boolean{
        return mail_regex.matches(email)
    }

    fun validateInput(){
        registerButton.isEnabled = emailEditText.text.length > 1 &&
                                   registerPasswordEdittext.text.length >= 6 &&
                                   registerAgainPasswordEdittext.text.length >= 6
    }

    fun displayWarning(){
        emailEditText.setBackgroundResource(R.drawable.underline_red)
        emailErrorTextview.text = "Your address is invalid or passwords doesn't match!"
        emailErrorTextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        emailErrorTextview.setTextColor(resources.getColor(R.color.pink))
    }

}