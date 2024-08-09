package com.macrew.medirydes.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.macrew.medirydes.R
import com.macrew.medirydes.dashboard.view.activities.MainActivity
import com.macrew.medirydes.login.view.LoginActivity
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.macrew.medirydes.utils.Static

class SplashActivity : AppCompatActivity() {
    private val WAIT_TIME = 1 * 1500L
    private var startedAt: Long = 0
    private var mEmailUser: String = ""
    private var isLogin: Boolean = false
    val static = Static()
    private lateinit var mWindow: Window

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startedAt = System.currentTimeMillis()


        proceedToNext()

    }

    private fun proceedToNext() {



        val elapsedTime = System.currentTimeMillis() - startedAt
        val remainingTime = WAIT_TIME - elapsedTime
        if (remainingTime > 0) {
            Handler().postDelayed({

                afterSplash()
            }, remainingTime)
        } else {
            afterSplash()
        }
    }

    private fun afterSplash() {

        try {
            isLogin = SharedPrefrencesUtils.isUserLogin()!!
        } catch (e: NullPointerException) {

        }
        if (!isLogin) {
            proceedLogin()
        } else {
            proceedMain()
        }

    }

    private fun proceedLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun proceedMain() {
        MainActivity.startActivity(this, "home")
        finishAffinity()
    }

    /* private fun proceedSignUp() {
         startActivity(Intent(this, SignUpNameActivity::class.java))
         //   finish()
     }*/


}