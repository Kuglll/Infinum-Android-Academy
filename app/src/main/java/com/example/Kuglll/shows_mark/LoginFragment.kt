package com.example.Kuglll.shows_mark

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    var userLogedIn = false
    val mail_regex = Regex("[^@]+@[^\\.]+\\..+")

    companion object{
        fun returnLoginFramgent() : Fragment{
            return LoginFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        userLogedIn = sharedPref.getBoolean(REMEMBERME, false)


        if(userLogedIn) startActivity(MainActivity.startMainActivity(activity!!))

        initOnClickListeners()
    }

    fun initOnClickListeners(){
        usernameEditText.doOnTextChanged { text, start, count, after ->  validateInput()}
        passwordEdittext.doOnTextChanged { text, start, count, after ->  validateInput()}

        textViewCreateAccount.setOnClickListener{displayRegisterFragment()}

        loginButton.setOnClickListener {
            if (emailMatchesRegex(usernameEditText.text.toString())) {
                checkForRememberMe()
                //TODO: Login user
                startActivity(MainActivity.startMainActivity(activity!!))
            } else {
                displayWarning()
            }
        }
    }

    fun validateInput(){
        val username = usernameEditText.text
        val password = passwordEdittext.text

        loginButton.isEnabled = username.length >= 1 && password.length >= 6
    }

    fun checkForRememberMe(){
        if (rememberMeCheckBox.isChecked){
            val sharedPref = requireActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putBoolean(REMEMBERME, true)
                apply()
            }
        }
    }

    fun displayRegisterFragment(){
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerLoginRegister, RegisterFragment.returnRegisterFragment(
                usernameEditText.text.toString(),
                passwordEdittext.text.toString()))
            .addToBackStack(LOGIN_FRAGMENT)
            .commit()
    }

    fun emailMatchesRegex(email: String): Boolean{
        return mail_regex.matches(email)
    }

    fun displayWarning(){
        usernameEditText.setBackgroundResource(R.drawable.underline_red)
        usernameErrorTextview.text = "Please enter a valid email address!"
        usernameErrorTextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        usernameErrorTextview.setTextColor(resources.getColor(R.color.pink))
    }

}
