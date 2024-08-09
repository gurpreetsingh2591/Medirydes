package com.macrew.medirydes.dashboard.view.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.FragmentType
import com.macrew.medirydes.dashboard.model.currentSchedule.ScheduleData
import com.macrew.medirydes.dashboard.view.activities.ProfileActivity
import com.macrew.medirydes.interfaces.MainFragmentListener
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    val static = Static()
    var name = ""
    var currentDate = ""

    var scheduleList: ArrayList<ScheduleData> = ArrayList<ScheduleData>()
    private lateinit var mainFragmentListener: MainFragmentListener

    companion object {
        lateinit var txtUName: TextView
        private val bundle = Bundle()
        private var ARG_PARAM = "CurrentScheduleList"
        fun getInstance(
            scheduleData: ArrayList<ScheduleData?>
        ): Fragment {
            val homeFragment = HomeFragment()
            bundle.putParcelableArrayList(ARG_PARAM, scheduleData)
            homeFragment.arguments = bundle

            return homeFragment
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        scheduleList = args?.getParcelableArrayList(ARG_PARAM)!!
        initUI(view)
    }


    @SuppressLint("SetTextI18n")
    private fun initUI(view: View) {
        txtUName = view.findViewById(R.id.txtName)
        name = SharedPrefrencesUtils.getUserName().toString()
        txtName.text = name


        if (scheduleList.size != 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                txtDate.text = static.dateFormatString(scheduleList[0].date.toString()) + ",  "
            } else {
                txtDate.text = scheduleList[0].date + ",  "
            }
            txtDay.text =
                static.capitalizeString(scheduleList[0].day!!)
            txtStartTime.text =
                scheduleList[0].start_time
            txtEndTime.text = scheduleList[0].end_time
            txtTrips.text =
                scheduleList[0].trips.size.toString()
            txtVehicleId.text =
                scheduleList[0].vehicle?.vin.toString()
        }


        cvFutureSchedule.setOnClickListener {

            object : CountDownTimer(300, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {
                    mainFragmentListener.showFragment(FragmentType.FUTURE_SHIFT_FRAGMENT, "")
                }
            }.start()


        }
        cvCurrentSchedule.setOnClickListener {
            object : CountDownTimer(300, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {
                    if (scheduleList.size != 0) {
                        mainFragmentListener.showFragment(FragmentType.CURRENT_SHIFT_FRAGMENT, "")
                    } else {
                        Toast.makeText(activity, "There are no schedules yet!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }.start()
        }
        cvReports.setOnClickListener {
            object : CountDownTimer(300, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {
                    mainFragmentListener.showFragment(FragmentType.REPORTS_FRAGMENT, "")
                }
            }.start()
        }
        cvProfile.setOnClickListener {
            object : CountDownTimer(300, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {
                    ProfileActivity.startActivity(requireActivity(), "")
                  //  mainFragmentListener.showFragment(FragmentType.PROFILE_FRAGMENT, "")
                }
            }.start()

        }

        cvRequest.setOnClickListener {
            object : CountDownTimer(300, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {
                    mainFragmentListener.showFragment(FragmentType.REQUEST_TIME_FRAGMENT, "")
                }
            }.start()
        }

    }

}