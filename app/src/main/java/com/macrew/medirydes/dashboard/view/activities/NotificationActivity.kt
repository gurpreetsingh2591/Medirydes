package com.macrew.medirydes.dashboard.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.macrew.medirydes.R
import kotlinx.android.synthetic.main.tab_bar.*

class NotificationActivity : AppCompatActivity() {

    companion object {
        fun startActivity(activity: Activity?, screen: String) {
            val intent = Intent(activity, NotificationActivity::class.java)
            intent.putExtra("screen", screen)
            activity?.startActivity(intent)
            //  activity?.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        initUI()
    }

    private fun initUI() {
        imgBack.visibility = View.VISIBLE
        imgNotification.visibility = View.GONE
        imgProfile.visibility = View.VISIBLE
        imgNavigation.visibility = View.GONE
        imgBack.setOnClickListener {
            finish()
        }
    }
}