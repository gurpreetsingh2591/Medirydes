package com.macrew.medirydes.dashboard.view.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Constants.TAG
import com.macrew.medirydes.annotation.FragmentType
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.DefaultModel
import com.macrew.medirydes.dashboard.model.currentSchedule.CurrentScheduleModel
import com.macrew.medirydes.dashboard.model.currentSchedule.ScheduleData
import com.macrew.medirydes.dashboard.viewModel.DashboardViewModel
import com.macrew.medirydes.interfaces.MainFragmentListener
import com.macrew.medirydes.retrofit.WebResponse
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.fragment_today_shift.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class CurrentScheduleFragment : Fragment() {
    val static = Static()
    var scheduleList: ArrayList<ScheduleData?> = ArrayList()
    var serverTime = ""
    private var finalTime = ""
    var milliseconds = ""
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var mainFragmentListener: MainFragmentListener

    companion object {
        private val bundle = Bundle()
        private var ARG_PARAM = "CurrentScheduleList"
        private var ARG_PARAM2 = "server_time"
        fun getInstance(
            scheduleData: ArrayList<ScheduleData?>, server_time: String
        ): Fragment {
            val currentScheduleFragment = CurrentScheduleFragment()
            bundle.putParcelableArrayList(ARG_PARAM, scheduleData)
            bundle.putString(ARG_PARAM2, server_time)
            currentScheduleFragment.arguments = bundle
            return currentScheduleFragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainFragmentListener = (activity as MainFragmentListener?)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_today_shift, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        scheduleList = args?.getParcelableArrayList(ARG_PARAM)!!
        serverTime = args.getString(ARG_PARAM2)!!
        initUI()
        initObserver()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun initUI() {

        if (scheduleList.size != 0) {
            txtNoList.visibility = View.GONE
            llDetail.visibility = View.VISIBLE

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                txtDate.text = static.dateFormatString(scheduleList[0]?.date.toString()) + ",  "
            } else {
                txtDate.text = scheduleList[0]?.date.toString() + ",  "
            }

            txtDay.text = static.capitalizeString(scheduleList[0]?.day!!)
            txtStartTime.text =
                scheduleList[0]?.start_time
            txtEndTime.text = scheduleList[0]?.end_time
            txtTrips.text =
                scheduleList[0]?.trips?.size.toString()
            txtVehicleId.text =
                scheduleList[0]?.vehicle?.vin.toString()

            if (scheduleList[0]?.status.equals(getString(R.string.started))) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    txtShiftBeginEnd.text = getString(R.string.shift_end_in)
                    txtTimer.text = static.timeCalculationString(
                        serverTime,
                        scheduleList[0]?.date.toString() + " " + scheduleList[0]?.end_time.toString() + ":00"
                    )
                }
            } else if (scheduleList[0]?.status.equals(getString(R.string.not_started))) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    txtShiftBeginEnd.text = getString(R.string.shift_begins_in)
                    txtTimer.text = static.timeCalculationString(
                        serverTime,
                        scheduleList[0]?.date.toString() + " " + scheduleList[0]?.start_time.toString() + ":00"
                    )
                }
            }


//            val date = scheduleList[0].date.toString() + " " + "$finalTime:00"
//            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//            val mDate: Date = simpleDateFormat.parse(date)
//            val timeInMilliseconds = mDate.time


            // val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            // val localDate = LocalDateTime.parse(date, formatter)
            //val timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
            // Log.d(TAG, "Date in milli :: FOR API >= 26 >>> $timeInMilliseconds")


//            object : CountDownTimer(timeInMilliseconds, 1000) {
//                override fun onTick(millisUntilFinished: Long) {
//                    val sec = (millisUntilFinished / 1000) % 60
//                    val min = (millisUntilFinished / (1000 * 60)) % 60
//                    val hr = (millisUntilFinished / (1000 * 60 * 60)) % 24
//                    val day = ((millisUntilFinished / (1000 * 60 * 60)) / 24).toInt()
//                    val formattedTimeStr = if (day > 1) "$hr h $min m $sec s"
//                    else "$hr h $min m $sec s"
//                    txtTimer.text = formattedTimeStr
//                }
//
//                override fun onFinish() {
//                    txtTimer.text = "Done!"
//                }
//            }.start()


        } else {
            llDetail.visibility = View.GONE
            txtNoList.visibility = View.VISIBLE
        }

        cvCurrentSchedule.setOnClickListener {
            mainFragmentListener.showFragment(FragmentType.TRIP_DETAIL_FRAGMENT, "")
            // ScheduleTripDetailActivity.startActivity(activity, "")
        }

        btnRequest.setOnClickListener {
            mainFragmentListener.showFragment(FragmentType.INSPECTION_FRAGMENT, "")
        }


        if (scheduleList.size != 0) {
            if (scheduleList[0]?.status.equals(getString(R.string.started))) {
                btnStartShift.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.red_color))
                btnStartShift.text = "End Shift"
                txtShiftBeginEnd.visibility = View.VISIBLE
                txtTimer.visibility = View.VISIBLE
            } else if (scheduleList[0]?.status.equals(getString(R.string.not_started))) {
                btnStartShift.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.app_base_color))
                btnStartShift.text = "Start Shift"
                txtShiftBeginEnd.visibility = View.VISIBLE
                txtTimer.visibility = View.VISIBLE
            } else {
                txtShiftBeginEnd.visibility = View.GONE
                txtTimer.visibility = View.GONE
                btnStartShift.text = "Shift Completed"
            }
        } else {
            Toast.makeText(activity, "There is no schedule Available!", Toast.LENGTH_SHORT)
                .show()
        }

        btnStartShift.setOnClickListener {
            if (scheduleList.size != 0) {
                if (scheduleList[0]?.status.equals(getString(R.string.started))) {
                    dashboardViewModel.callGetStartScheduleApi(
                        scheduleList[0]?.id.toString(),
                        getString(R.string.completed)
                    )
                } else if (scheduleList[0]?.status.equals(getString(R.string.not_started))) {
                    dashboardViewModel.callGetStartScheduleApi(
                        scheduleList[0]?.id.toString(),
                        getString(R.string.started)
                    )
                } else {
                    Toast.makeText(activity, "Schedule Already complete", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(activity, "There are no schedules yet!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    //initialise Observer
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        dashboardViewModel.isLoading.observe(requireActivity()) { aBoolean ->
            if (aBoolean)
                progressLoading.visibility = View.VISIBLE
            else
                progressLoading.visibility = View.GONE
        }


        //Start/End  schedule response
        dashboardViewModel.responseStartScheduleData.observe(
            requireActivity()
        ) { ResponseLogin: WebResponse<CurrentScheduleModel> ->
            if (ResponseLogin.status == Status.SUCCESS) {
                Toast.makeText(requireActivity(), ResponseLogin.errorMsg, Toast.LENGTH_SHORT).show()
                if (ResponseLogin.data?.result?.schedules?.get(0)?.status.equals(getString(R.string.started))) {

                    if (ResponseLogin.data?.result?.schedules?.size != 0) {
                        scheduleList = arrayListOf()
                        scheduleList = ResponseLogin.data?.result?.schedules!!
                        serverTime = ResponseLogin.data?.result?.server_time!!
                        SharedPrefrencesUtils.setMobilityType(ResponseLogin.data?.result?.schedules!![0]?.vehicle?.name!!)
                    } else {
                        scheduleList = ArrayList()
                    }
                    initUI()
                } else {
                    initUI()
                }
            }
            if (ResponseLogin.status == Status.FAILURE) {
                Toast.makeText(requireActivity(), ResponseLogin.errorMsg, Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }


}