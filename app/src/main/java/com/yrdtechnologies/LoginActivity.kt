package com.yrdtechnologies

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.yrdtechnologies.vm.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.email
import kotlinx.android.synthetic.main.activity_login.emailTextInputLayout
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.passwordTextInputLayout

class LoginActivity : ActivityBase(){

    lateinit var viewModel: LoginViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(LoginViewModel::class.java)
        // Set up the login form.
        password.setOnEditorActionListener(TextView.OnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                if(viewModel.attemptLogin(emailTextInputLayout,passwordTextInputLayout,email,password)){
                    goAhead()
                }
                return@OnEditorActionListener true
            }
            false
        })
        val mEmailSignInButton = findViewById<View>(R.id.email_sign_in_button) as Button
        mEmailSignInButton.setOnClickListener { if(viewModel.attemptLogin(emailTextInputLayout,passwordTextInputLayout,email,password)){
            goAhead()
        } }
        showSoftInput(email)
    }

    private fun goAhead(){
        val intent = Intent(this@LoginActivity, ActivityLanding::class.java)
        putBoolean(isUserLoggedIn, true)
        this@LoginActivity.startActivity(intent)
        finish()
    }

    companion object {
        const val isUserLoggedIn = "isUserLoggedIn"
    }
}