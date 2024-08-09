package com.macrew.medirydes.forgotPassword.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Constants
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.extra.WebResponse
import com.macrew.medirydes.login.model.login.LoginModel
import com.macrew.medirydes.login.view_model.LoginViewModel
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_loading.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private val static = Static()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initUI()
        initObserver()
    }

    private fun initUI() {

        btnLogin.setOnClickListener {
            isValidCredentials(edtEmail.text.toString(), edtPassword.text.toString())


        }
    }

    override fun onPause() {
        super.onPause()
        edtEmail.error = null //removes error
        edtEmail.clearFocus() //clear focus from edittext
        edtPassword.error = null //removes error
        edtPassword.clearFocus() //clear focus from edittext
    }

    //initialise Observer
    private fun initObserver() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                progressLoading.visibility = View.VISIBLE
            else
                progressLoading.visibility = View.GONE
        }

        loginViewModel.responseLoginData.observe(this,
            Observer<WebResponse<LoginModel>> { ResponseLogin: WebResponse<LoginModel> ->
                if (ResponseLogin.status == Status.SUCCESS) {



                }
                if (ResponseLogin.status == Status.FAILURE) {
                    Toast.makeText(this, ResponseLogin.errorMsg, Toast.LENGTH_SHORT).show()
                }

            }
        )
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                progressLoading!!.visibility = View.VISIBLE
            else
                progressLoading!!.visibility =
                    View.GONE
        }
    }

    private fun isValidCredentials(email: String, password: String): Boolean {

        if (email.isEmpty()) {
            edtEmail.error = Constants.ENTER_EMAIL
            edtEmail.isFocusable = true
            edtEmail.requestFocus()
            return false
        } else if (password.isEmpty()) {
            edtPassword.error = Constants.ENTER_PASSWORD
            edtPassword.isFocusable = true
            edtPassword.requestFocus()
            return false
        } else if (!static.isValidEmail(email)) {
            edtEmail.error = Constants.ENTER_VALID_EMAIL
            edtEmail.isFocusable = true
            edtEmail.requestFocus()
            return false
        } else {
            // call signIn API
            loginViewModel.callGetLoginApi(
                edtEmail.text.toString(),
                edtPassword.text.toString(),
                ""
            )

            return true
        }


    }
}