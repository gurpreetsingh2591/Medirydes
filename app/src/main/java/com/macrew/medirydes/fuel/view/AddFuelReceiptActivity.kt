package com.macrew.medirydes.fuel.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.macrew.medirydes.R
import kotlinx.android.synthetic.main.tab_bar.*

class AddFuelReceiptActivity : AppCompatActivity() {

    companion object {
        fun startActivity(activity: Activity?, screen: String) {
            val intent = Intent(activity, AddFuelReceiptActivity::class.java)
            intent.putExtra("screen", screen)
            activity?.startActivity(intent)
            //  activity?.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fuel_reciept)
        initUI()
    }

    private fun initUI() {
        imgBack.visibility = View.VISIBLE
        imgNavigation.visibility = View.GONE
        imgBack.setOnClickListener {
            finish()
        }
    }
}
