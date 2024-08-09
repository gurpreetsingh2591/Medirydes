package com.macrew.medirydes.reports.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Constants
import com.macrew.medirydes.dashboard.model.reports.Trips
import com.macrew.medirydes.dashboard.view.activities.NotificationActivity
import com.macrew.medirydes.dashboard.view.activities.ProfileActivity
import com.macrew.medirydes.tripDetail.viewModel.TripViewModel
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.macrew.medirydes.utils.Static
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_report_trip.*
import kotlinx.android.synthetic.main.fragment_report_trip.rvTripList
import kotlinx.android.synthetic.main.tab_bar.*

class ReportTripListActivity : AppCompatActivity() {
    var strTime = ""
    var tripList: ArrayList<Trips?> = ArrayList()
    var date = ""
    var day = ""
    var endTime = ""
    var startTime = ""
    private lateinit var tripViewModel: TripViewModel
    private val static = Static()
    private lateinit var reportTripListAdapter: ReportTripListAdapter

    companion object {
        fun startActivity(
            activity: Activity?,
            trips: ArrayList<Trips?>,
            date: String,
            day: String,
            startDate: String,
            endTime: String
        ) {
            val bundle = Bundle()
            val intent = Intent(activity, ReportTripListActivity::class.java)
            bundle.putParcelableArrayList(Constants.ARG_PARAM_TRIP_LIST, trips)
            bundle.putString(Constants.ARG_PARAM_DATE, date)
            bundle.putString(Constants.ARG_PARAM_DAY, day)
            bundle.putString(Constants.ARG_PARAM_START_TIME, startDate)
            bundle.putString(Constants.ARG_PARAM_END_TIME, endTime)
            intent.putExtra("bundle", bundle)
            activity?.startActivity(intent)
            //  activity?.finish()
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_report_trip)
        val bundle = intent.getBundleExtra("bundle")
        tripList = bundle?.getParcelableArrayList(Constants.ARG_PARAM_TRIP_LIST)!!
        date = bundle.getString(Constants.ARG_PARAM_DATE)!!
        day = bundle.getString(Constants.ARG_PARAM_DAY)!!
        startTime = bundle.getString(Constants.ARG_PARAM_START_TIME)!!
        endTime = bundle.getString(Constants.ARG_PARAM_END_TIME)!!

        txtDateDay.text = static.dateFormatString(date) + " " + static.capitalizeString(day) + ", "
        txtStartEndTime.text = "$startTime - $endTime"

        initUI()
        initRV()
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        imgBack.visibility = View.VISIBLE
        imgNavigation.visibility = View.GONE
        imgBack.setOnClickListener {
            finish()
        }

        if (!SharedPrefrencesUtils.getUserImage().equals("")) {
            Picasso.get().load(SharedPrefrencesUtils.getUserImage()).into(imgProfile)
        }

        imgProfile.setOnClickListener {
            // imgProfile.visibility = View.VISIBLE
            ProfileActivity.startActivity(this, "")
        }
        imgNotification.setOnClickListener {
            //imgProfile.visibility = View.VISIBLE
            NotificationActivity.startActivity(this, "")
        }
    }

    private fun initRV() {
        rvTripList.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        reportTripListAdapter = ReportTripListAdapter(tripList, this,
            object : ReportTripListAdapter.OnItemCheckListener {
                override fun onItemView(id: String?, pos: Int) {
                    //  mainFragmentListener.showFragment(FragmentType.TRIP_DETAIL_FRAGMENT,"")
                    ReportTripDetailActivity
                        .startActivity(
                            this@ReportTripListActivity,
                            tripList[pos]!!,
                            date,
                            day,
                            startTime,
                            endTime
                        )
                }

                override fun onItemEdit(id: String?) {
                    TODO("Not yet implemented")
                }

                override fun onItemDelete(id: String?) {
                    TODO("Not yet implemented")
                }
            }
        )
        rvTripList.adapter = reportTripListAdapter
    }

}