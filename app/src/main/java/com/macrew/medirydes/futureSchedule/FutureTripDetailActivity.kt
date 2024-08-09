package com.macrew.medirydes.futureSchedule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Constants
import com.macrew.medirydes.dashboard.model.future.Trips
import com.macrew.medirydes.dashboard.view.activities.NotificationActivity
import com.macrew.medirydes.dashboard.view.activities.ProfileActivity
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.macrew.medirydes.utils.Static
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_schedule_trip_detail.*
import kotlinx.android.synthetic.main.tab_bar.*

class FutureTripDetailActivity : AppCompatActivity() {
    var strTime = ""
    lateinit var trips: Trips
    var date = ""
    var day = ""
    var endTime = ""
    var startTime = ""
    private val static = Static()

    companion object {
        fun startActivity(
            activity: Activity?,
            trips: Trips,
            date: String,
            day: String,
            startDate: String,
            endTime: String
        ) {
            val bundle = Bundle()
            val intent = Intent(activity, FutureTripDetailActivity::class.java)
            bundle.putParcelable(Constants.ARG_PARAM_TRIP_Object, trips)
            bundle.putString(Constants.ARG_PARAM_DATE, date)
            bundle.putString(Constants.ARG_PARAM_DAY, day)
            bundle.putString(Constants.ARG_PARAM_START_TIME, startDate)
            bundle.putString(Constants.ARG_PARAM_END_TIME, endTime)
            intent.putExtra("bundle", bundle)
            activity?.startActivity(intent)
            //  activity?.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_trip_detail)
        val bundle = intent.getBundleExtra("bundle")
        trips = bundle?.getParcelable(Constants.ARG_PARAM_TRIP_Object)!!
        date = bundle.getString(Constants.ARG_PARAM_DATE)!!
        day = bundle.getString(Constants.ARG_PARAM_DAY)!!
        startTime = bundle.getString(Constants.ARG_PARAM_START_TIME)!!
        endTime = bundle.getString(Constants.ARG_PARAM_END_TIME)!!



        initUI()

    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        imgBack.visibility = View.VISIBLE
        imgNavigation.visibility = View.GONE
        btnStartTrip.visibility = View.GONE
        imgBack.setOnClickListener {
            finish()
        }
        if (SharedPrefrencesUtils.getUserImage() != "") {
            Glide.with(this).load(SharedPrefrencesUtils.getUserImage()).into(imgProfile);
           // Picasso.get().load(SharedPrefrencesUtils.getUserImage()).into(imgProfile)
        }
        imgProfile.setOnClickListener {
            // imgProfile.visibility = View.VISIBLE
            ProfileActivity.startActivity(this, "")
        }
        imgNotification.setOnClickListener {
            //imgProfile.visibility = View.VISIBLE
            NotificationActivity.startActivity(this, "")
        }
        txtRiderName.text = trips.user?.name
        txtRiderPhone.text = trips.origin_phone
        txtPickUpLocation.text = trips.destination_name+", "+trips.origin_street_address+", "+
                trips.origin_suite+", "+trips.origin_city+", "+trips.origin_state+", "+
                trips.origin_postal_code+", "+ trips.origin_county


        txtPickupTime.text = trips.trip_time


        txtDropLocation.text = trips.destination_name+", "+trips.destination_street_address+", "+
                trips.destination_suite+", "+trips.destination_city+", "+trips.destination_state
        trips.destination_postal_code+", "+trips.destination_county


        txtType.text = trips.requested_trip
        txtConfirmationNumber.text=trips.confirmation_number
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            txtDayDate.text = static.capitalizeString(day) + " - " + static.dateFormat2String(date)
        } else {


            txtDayDate.text = static.capitalizeString(day) + ", " + date
        }



    }


}