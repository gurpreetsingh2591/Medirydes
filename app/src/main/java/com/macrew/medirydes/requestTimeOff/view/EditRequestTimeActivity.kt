package com.macrew.medirydes.requestTimeOff.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.DefaultModel
import com.macrew.medirydes.dashboard.model.avalibilty.WorkDay
import com.macrew.medirydes.dashboard.view.activities.NotificationActivity
import com.macrew.medirydes.dashboard.view.activities.ProfileActivity
import com.macrew.medirydes.requestTimeOff.viewModel.EditAvailabilityViewModel
import com.macrew.medirydes.retrofit.WebResponse
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import kotlinx.android.synthetic.main.activity_edit_request.*
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.tab_bar.*
import org.json.JSONArray
import org.json.JSONObject


class EditRequestTimeActivity : AppCompatActivity() {

    private lateinit var workDay: String
    private lateinit var editAvailabilityViewModel: EditAvailabilityViewModel
    private lateinit var editAvailabilityAdapter: EditAvailabilityAdapter
    private var avalObj: ArrayList<JSONObject> = arrayListOf()
    var workDayList: ArrayList<WorkDay> = arrayListOf()
    lateinit var workDayObj: WorkDay

    companion object {
        private var ARG_PARAM = "weekNameAvailability"

        fun startActivity(activity: Activity?, workDay: String) {
            val intent = Intent(activity, EditRequestTimeActivity::class.java)
            intent.putExtra(ARG_PARAM, workDay)
            activity?.startActivity(intent)
            //  activity?.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_request)
        initObserver()
        initUI()
    }

    private fun initUI() {
        imgBack.visibility = View.VISIBLE
        imgNavigation.visibility = View.GONE
        imgBack.setOnClickListener {
            finish()
        }
        if (SharedPrefrencesUtils.getUserImage() != "") {
            Glide.with(this).load(SharedPrefrencesUtils.getUserImage()).into(imgProfile);
        }

        imgProfile.setOnClickListener {
            // imgProfile.visibility = View.VISIBLE
            ProfileActivity.startActivity(this, "")
        }
        imgNotification.setOnClickListener {
            //imgProfile.visibility = View.VISIBLE
            NotificationActivity.startActivity(this, "")
        }


        workDay = intent.getStringExtra(ARG_PARAM)!!

        var jsonElement: JSONObject
        val gson = Gson()

        var jsonArray = JSONArray(workDay)
        avalObj = arrayListOf()
        (0 until jsonArray.length()).forEach {
            var obj = jsonArray.getJSONObject(it)
            avalObj.add(obj)
            Log.e("avalObj----", "" + avalObj)
        }

        jsonElement = avalObj[0]
        val monday = jsonElement.get("monday")
        val mondayData = gson.fromJson(monday.toString(), WorkDay::class.java)
        workDayObj = WorkDay(
            mondayData.from_time.toString(),
            mondayData.to_time.toString(),
            mondayData.all_day.toString()
        )
        workDayList.add(workDayObj)

        jsonElement = avalObj[1]
        val tuesday = jsonElement.get("tuesday")
        val tuesdayData = gson.fromJson(tuesday.toString(), WorkDay::class.java)
        workDayObj = WorkDay(
            tuesdayData.from_time.toString(),
            tuesdayData.to_time.toString(),
            tuesdayData.all_day.toString()
        )

        workDayList.add(workDayObj)

        jsonElement = avalObj[2]
        val wednesday = jsonElement.get("wednesday")
        val wednesdayData = gson.fromJson(wednesday.toString(), WorkDay::class.java)
        workDayObj = WorkDay(
            wednesdayData.from_time.toString(),
            wednesdayData.to_time.toString(),
            wednesdayData.all_day.toString()
        )
        workDayList.add(workDayObj)

        jsonElement = avalObj[3]
        val thursday = jsonElement.get("thursday")
        val thursdayData = gson.fromJson(thursday.toString(), WorkDay::class.java)

        workDayObj = WorkDay(
            thursdayData.from_time.toString(),
            thursdayData.to_time.toString(),
            thursdayData.all_day.toString()
        )
        workDayList.add(workDayObj)

        jsonElement = avalObj[4]
        val friday = jsonElement.get("friday")
        val fridayData = gson.fromJson(friday.toString(), WorkDay::class.java)
        workDayObj = WorkDay(
            fridayData.from_time.toString(),
            fridayData.to_time.toString(),
            fridayData.all_day.toString()
        )

        workDayList.add(workDayObj)

        jsonElement = avalObj[5]

        val saturday = jsonElement.get("saturday")
        val saturdayData = gson.fromJson(saturday.toString(), WorkDay::class.java)

        workDayObj = WorkDay(
            saturdayData.from_time.toString(),
            saturdayData.to_time.toString(),
            saturdayData.all_day.toString()
        )
        workDayList.add(workDayObj)

        jsonElement = avalObj[6]
        val sunday = jsonElement.get("sunday")
        val sundayData = gson.fromJson(sunday.toString(), WorkDay::class.java)
        workDayObj = WorkDay(
            sundayData.from_time.toString(),
            sundayData.to_time.toString(),
            sundayData.all_day.toString()
        )
        workDayList.add(workDayObj)
        initRVReArrange()
        imgBack.visibility = View.VISIBLE
        imgNavigation.visibility = View.GONE
        imgBack.setOnClickListener {
            finish()
        }
        initRVReArrange()



        btnEditAvailabilitySave.setOnClickListener {
            avalObj = arrayListOf()

            for (i in workDayList.indices) {

                val json = JSONObject()
                val jsonObj = JSONObject()

                jsonObj.put("all_day", workDayList[i].all_day)
                jsonObj.put("from_time", workDayList[i].from_time)
                jsonObj.put("to_time", workDayList[i].to_time)

                if (i == 0) {
                    json.put("monday", jsonObj);
                }
                if (i == 1) {
                    json.put("tuesday", jsonObj);
                }
                if (i == 2) {
                    json.put("wednesday", jsonObj);
                }
                if (i == 3) {
                    json.put("thursday", jsonObj);
                }
                if (i == 4) {
                    json.put("friday", jsonObj);
                }
                if (i == 5) {
                    json.put("saturday", jsonObj);
                }
                if (i == 6) {
                    json.put("sunday", jsonObj);
                }

                avalObj.add(json)
            }
            val jsonElements = JSONArray(avalObj)
             editAvailabilityViewModel.callGetEditAvailabilityApi(jsonElements.toString())
        }
    }

    private fun initRVReArrange() {
        rvEditAvailability.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        editAvailabilityAdapter = EditAvailabilityAdapter(workDayList, this,
            object : EditAvailabilityAdapter.OnItemCheckListener {
                override fun onItemStartView(startTime: String?, position: Int) {

                    workDayList[position].from_time = startTime
                    editAvailabilityAdapter.notifyDataSetChanged()
                }

                override fun onItemEndEdit(endTime: String?, position: Int) {
                    workDayList[position].to_time = endTime
                    editAvailabilityAdapter.notifyDataSetChanged()
                }

                override fun onItemAllDayDelete(allDay: String?, position: Int) {
                    workDayList[position].all_day = allDay
                    editAvailabilityAdapter.notifyDataSetChanged()
                }
            }
        )
        rvEditAvailability.adapter = editAvailabilityAdapter
    }

    //initialise Observer
    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        editAvailabilityViewModel =
            ViewModelProvider(this).get(EditAvailabilityViewModel::class.java)
        editAvailabilityViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                progressLoading.visibility = View.VISIBLE
            else
                progressLoading.visibility = View.GONE
        }


        /*edit availability*/
        editAvailabilityViewModel.responseEditAvailabilityData.observe(
            this
        ) { ResponseLogin: WebResponse<DefaultModel> ->
            if (ResponseLogin.status == Status.SUCCESS) {
                Toast.makeText(this, ResponseLogin.errorMsg, Toast.LENGTH_SHORT).show()

            }
            if (ResponseLogin.status == Status.FAILURE) {
                Toast.makeText(this, ResponseLogin.errorMsg, Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }
}