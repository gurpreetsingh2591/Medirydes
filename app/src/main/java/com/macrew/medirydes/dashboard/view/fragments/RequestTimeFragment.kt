package com.macrew.medirydes.dashboard.view.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.avalibilty.AvailabilityModel
import com.macrew.medirydes.dashboard.viewModel.DashboardViewModel
import com.macrew.medirydes.requestTimeOff.view.EditRequestTimeActivity
import com.macrew.medirydes.retrofit.WebResponse
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.fragment_reports.*
import kotlinx.android.synthetic.main.fragment_request_time.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class RequestTimeFragment : Fragment() {
    val static = Static()
    private lateinit var dashboardViewModel: DashboardViewModel
    var avalObj: ArrayList<JSONObject> = arrayListOf()
    var avalStr = ""

    companion object {
        private val bundle = Bundle()

        private var ARG_PARAM = "currencyList"

        fun getInstance(): Fragment {
            return RequestTimeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_request_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObserver()
        dashboardViewModel.callGetAvailabilityListApi()
        val args = arguments
        initUI()
    }

    fun initUI() {
        btnEditAvailability.setOnClickListener {
            EditRequestTimeActivity.startActivity(activity, avalStr)
        }
    }


    //initialise Observer
    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        dashboardViewModel.isLoading.observe(requireActivity()) { aBoolean ->
            if (aBoolean)
                progressLoading.visibility = View.VISIBLE
            else
                progressLoading.visibility = View.GONE
        }

        dashboardViewModel.responseAvailabilityData.observe(
            requireActivity()
        ) { ResponseLogin: WebResponse<AvailabilityModel> ->
            if (ResponseLogin.status == Status.SUCCESS) {
                avalStr = ResponseLogin.data?.result?.availability?.work_days!!
                var jsonArray = JSONArray(ResponseLogin.data?.result?.availability?.work_days!!)
                avalObj = arrayListOf()
                (0 until jsonArray.length()).forEach {
                    var obj = jsonArray.getJSONObject(it)
                    avalObj.add(obj)
                    Log.e("avalObj----", "" + avalObj)
                }


                //var tutorials: Array<WorkDay> = gson.fromJson(monday, arrayTutorialType)
            }
            if (ResponseLogin.status == Status.FAILURE) {
                Toast.makeText(requireActivity(), ResponseLogin.errorMsg, Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }


}