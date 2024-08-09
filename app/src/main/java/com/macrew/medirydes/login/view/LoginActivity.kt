package com.macrew.medirydes.login.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Constants
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.view.activities.MainActivity
import com.macrew.medirydes.extra.WebResponse


import com.macrew.medirydes.login.model.login.LoginModel
import com.macrew.medirydes.login.model.company.CompanyModel
import com.macrew.medirydes.utils.Static
import com.macrew.medirydes.login.view_model.LoginViewModel
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_loading.*


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private val static = Static()
    var companyCode = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initUI()
        initObserver()
    }

    private fun initUI() {
        btnCodeConfirm.setOnClickListener {
            isValidCompanyCode(edtCompanyCode.text.toString())
        }
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
            { ResponseLogin: WebResponse<LoginModel> ->
                if (ResponseLogin.status == Status.SUCCESS) {
                    SharedPrefrencesUtils.setUserLogin(
                        true,
                        ResponseLogin.data?.result!!.user?.id.toString(),
                        ResponseLogin.data?.result!!.user?.name,
                        ResponseLogin.data?.result!!.user?.email,
                        ResponseLogin.data?.result!!.user?.phone.toString(),
                        ResponseLogin.data?.result!!.user?.token,
                        ResponseLogin.data?.result!!.user?.user_detail!!.gender,
                        ResponseLogin.data?.result!!.user?.user_detail!!.age,
                        ResponseLogin.data?.result!!.user?.user_detail!!.license_number,
                        ResponseLogin.data?.result!!.user?.user_detail!!.image_url,
                    )


                    MainActivity.startActivity(this, "home")
                    finishAffinity()
                }
                if (ResponseLogin.status == Status.FAILURE) {
                    Toast.makeText(this, ResponseLogin.errorMsg, Toast.LENGTH_SHORT).show()
                }

            }
        )


        loginViewModel.responseCompanyData.observe(this,
            { ResponseLogin: WebResponse<CompanyModel> ->
                if (ResponseLogin.status == Status.SUCCESS) {
                    SharedPrefrencesUtils.setCompanyDetail(
                        ResponseLogin.data?.result?.company!!.id,
                        ResponseLogin.data?.result?.company!!.company_name,
                        ResponseLogin.data?.result?.company!!.company_code,
                        ResponseLogin.data?.result?.company!!.phone,
                        ResponseLogin.data?.result?.company!!.address,
                        "",

                        )
                    cvCompanyCode.visibility = View.GONE
                    cvLoginCredantial.visibility = View.VISIBLE
                    companyCode = ResponseLogin.data?.result?.company!!.id.toString()
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

    private fun isValidCompanyCode(code: String): Boolean {

        if (code.isEmpty()) {
            edtCompanyCode.error = Constants.ENTER_CODE
            edtCompanyCode.isFocusable = true
            edtCompanyCode.requestFocus()
            return false
        } else {

            loginViewModel.callGetCompanyVerifyApi(edtCompanyCode.text.toString())


            return true
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
                companyCode
            )

            return true
        }


    }

}