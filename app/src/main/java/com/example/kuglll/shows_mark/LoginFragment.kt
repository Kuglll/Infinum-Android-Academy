package com.example.kuglll.shows_mark

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.kuglll.shows_mark.dataClasses.DataViewModel
import com.example.kuglll.shows_mark.databinding.FragmentLoginBinding
import com.example.kuglll.shows_mark.utils.LoginRequest
import com.example.kuglll.shows_mark.utils.LoginResult
import com.example.kuglll.shows_mark.utils.Singleton
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TOKEN = "TOKEN"

class LoginFragment : Fragment() {

    var token: String? = null
    var userLogedIn = false
    val mail_regex = Regex("[^@]+@[^\\.]+\\..+")
    lateinit var viewModel: DataViewModel

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
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(DataViewModel::class.java)
        }

        val binding: FragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.viewmodel = viewModel
        binding.setLifecycleOwner { lifecycle }
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            val sharedPref = it.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            userLogedIn = sharedPref.getBoolean(REMEMBERME, false)
            token = sharedPref.getString(TOKEN, null)
            Log.d("TOKEN", token)
        }

        if(userLogedIn) {
            if(token != null){
                activity?.let {
                    startActivity(MainActivity.startMainActivity(it))
                }
            }
        }

        initOnClickListeners()
    }

    fun initOnClickListeners(){
        usernameEditText.doOnTextChanged { text, start, count, after ->  validateInput()}
        passwordEdittext.doOnTextChanged { text, start, count, after ->  validateInput()}

        textViewCreateAccount.setOnClickListener{displayRegisterFragment()}

        loginButton.setOnClickListener {
            if (emailMatchesRegex(usernameEditText.text.toString())) {
                checkForRememberMe()
                loginUser(usernameEditText.text.toString(), passwordEdittext.text.toString())
            } else {
                displayWarning()
            }
        }
    }

    fun loginUser(email: String, password: String){
        val dialog: AlertDialog = SpotsDialog.Builder().setContext(context).build()
        dialog.show()
        Singleton.service.login(LoginRequest(email, password)).enqueue(object : Callback<LoginResult>{
            override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                dialog.dismiss()
                Toast.makeText(context, "For successful login you need internet access!", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                dialog.dismiss()
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        token = body.data.token
                        storeToken(token)
                        activity?.let {
                            startActivity(MainActivity.startMainActivity(it))
                        }
                    }
                }
            }
        })
    }

    fun storeToken(token: String?){
        activity?.let {
            val sharedPref = it.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putString(TOKEN, token)
                apply()
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
            activity?.let {
                val sharedPref = it.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE) ?: return
                with (sharedPref.edit()) {
                    putBoolean(REMEMBERME, true)
                    apply()
                }
            }
        }
    }

    fun displayRegisterFragment(){
        viewModel.username.value = usernameEditText.text.toString()
        viewModel.password.value = passwordEdittext.text.toString()
        activity?.let {
            it.supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainerLoginRegister,
                    RegisterFragment.returnRegisterFragment()
                )
                .addToBackStack(LOGIN_FRAGMENT)
                .commit()
        }
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
