package com.yrdtechnologies.vm

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout
import com.yrdtechnologies.R

class LoginViewModel:ViewModel(){

    fun attemptLogin(emailTextInputLayout:TextInputLayout,passwordTextInputLayout:TextInputLayout,
    email:EditText,password:EditText):Boolean {
        // Reset errors.
        emailTextInputLayout.error = null
        passwordTextInputLayout.error = null

        // Store values at the time of the login attempt.
        val emailX = email.text.toString()
        val passwordX = password.text.toString()
        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordX) && !isPasswordValid(passwordX)) {
            passwordTextInputLayout.error = passwordTextInputLayout.context.getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailX)) {
            emailTextInputLayout.error = passwordTextInputLayout.context.getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailX)) {
            emailTextInputLayout.error = passwordTextInputLayout.context.getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
            return false
        } else {
            for (credential in DUMMY_CREDENTIALS) {
                val pieces = credential.split(":").toTypedArray()
                if (pieces[0] == emailX) {
                    // Account exists, return true if the password matches.
                    if( pieces[1] == passwordX){

                        return true
                    }
                }
            }
            passwordTextInputLayout.error = passwordTextInputLayout.context.getString(R.string.error_incorrect_password)
            password.requestFocus()
        }
        return false
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }
    private val DUMMY_CREDENTIALS = arrayOf(
            "reviwer-support@udacity.com:hello","foo@example.com:hello", "bar@example.com:world", "testuser@yrdtechnologies.com:12345"
    )
}