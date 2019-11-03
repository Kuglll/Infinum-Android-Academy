package com.example.Kuglll.shows_mark

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.toolbar.*

const val USERNAME_REGISTER = "USERNAME"
const val PASSWORD_REGISTER = "PASSWORD"

class RegisterFragment : Fragment(){

    val mail_regex = Regex("[^@]+@[^\\.]+\\..+")

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

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbarTitle.text = "Register"

        toolbar.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                activity!!.onBackPressed()
            }
        })

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

        registerButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if(passwordsMatch() && emailMatchesRegex()){
                    startActivity(MainActivity.startMainActivity(activity!!))
                } else{
                    displayWarning()
                }
            }
        })
    }

    fun passwordsMatch(): Boolean{
        return registerPasswordEdittext.text.toString() == registerAgainPasswordEdittext.text.toString()
    }

    fun emailMatchesRegex(): Boolean{
        return mail_regex.matches(emailEditText.text)
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