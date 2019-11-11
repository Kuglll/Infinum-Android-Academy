package com.example.kuglll.shows_mark

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.kuglll.shows_mark.dataClasses.DataViewModel
import com.example.kuglll.shows_mark.databinding.FragmentRegisterBinding
import com.example.kuglll.shows_mark.utils.RegisterRequest
import com.example.kuglll.shows_mark.utils.Singleton
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment(){

    val mail_regex = Regex("[^@]+@[^\\.]+\\..+")
    lateinit var viewModel: DataViewModel

    companion object {
        fun returnRegisterFragment(): Fragment {
            return RegisterFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(requireActivity()).get(DataViewModel::class.java)

        val binding: FragmentRegisterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.viewmodel = viewModel
        binding.setLifecycleOwner { lifecycle }
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarTitle.text = "Register"

        validateInput()

        initOnClickListeners()
    }


    fun initOnClickListeners(){
        emailEditText.doOnTextChanged { text, start, count, after ->  validateInput()}
        registerPasswordEdittext.doOnTextChanged { text, start, count, after ->  validateInput()}
        registerAgainPasswordEdittext.doOnTextChanged { text, start, count, after ->  validateInput()}

        toolbar.setNavigationOnClickListener{ activity!!.onBackPressed() }

        registerButton.setOnClickListener{
            if(passwordsMatch() && emailMatchesRegex(emailEditText.text.toString())){
                registerUser(emailEditText.text.toString(), registerPasswordEdittext.text.toString())
            } else{
                displayWarning()
            }
        }
    }

    fun registerUser(email : String, password: String){
        Singleton.createRequest().register(RegisterRequest(email, password)).enqueue(object : Callback<Any>{
            override fun onFailure(call: Call<Any>, t: Throwable) {
                //TODO: Handle failure
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>){
                if(response.isSuccessful){
                    // how to get each header: response.headers().get("name")
                    val body = response.body()
                    if(body != null){
                        startActivity(WelcomeActivity.startWelcomeActivity(activity!!, emailEditText.text.toString()))
                    }
                }
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