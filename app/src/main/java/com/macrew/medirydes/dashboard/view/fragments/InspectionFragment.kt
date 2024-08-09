package com.macrew.medirydes.dashboard.view.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.DefaultModel
import com.macrew.medirydes.dashboard.model.InspectionJson
import com.macrew.medirydes.dashboard.view.adapters.InspectionListAdapter
import com.macrew.medirydes.dashboard.viewModel.DashboardViewModel
import com.macrew.medirydes.interfaces.MainFragmentListener
import com.macrew.medirydes.retrofit.WebResponse
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.fragment_inspection_checklist.*


class InspectionFragment : Fragment() {
    val static = Static()
    private lateinit var mainFragmentListener: MainFragmentListener
    var scheduleId = ""
    var vehicleId = ""
    private lateinit var progressLoading: FrameLayout
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var inspectionListAdapter: InspectionListAdapter
    private lateinit var inspectionList: ArrayList<InspectionJson?>
    lateinit var checkedInspectionList: HashMap<String?, String?>

    //    val selectedToggles = toggleButtonLayout.selectedToggles()
    companion object {
        private val bundle = Bundle()
        private var ARG_PARAM = "schedule_id"
        private var ARG_PARAM1 = "vehicle_id"

        fun getInstance(scheduleID: String, vehicleId: String): Fragment {
            val inspectionFragment = InspectionFragment()
            bundle.putString(ARG_PARAM, scheduleID)
            bundle.putString(ARG_PARAM1, vehicleId)
            inspectionFragment.arguments = bundle
            return inspectionFragment
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
        return inflater.inflate(R.layout.fragment_inspection_checklist, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressLoading=view.findViewById(R.id.progressLoading)
        initObserver()
        dashboardViewModel.callGetInspectionJson()
        val args = arguments
        scheduleId = args!!.getString(ARG_PARAM).toString()
        vehicleId = args.getString(ARG_PARAM1).toString()

        rgBreaks.setOnCheckedChangeListener { radioGroup, i ->
            val rb = view.findViewById(i) as RadioButton
            if (rb.text.equals("Yes")) {
                rbBreakYes.setTextColor(Color.WHITE)
                rbBreakNo.setTextColor(Color.BLACK)
            } else {
                rbBreakYes.setTextColor(Color.BLACK)
                rbBreakNo.setTextColor(Color.WHITE)
            }
        }

        rgParkingBreaks.setOnCheckedChangeListener { radioGroup, i ->
            val rb = view.findViewById(i) as RadioButton
            if (rb.text.equals("Yes")) {
                rbParBreakYes.setTextColor(Color.WHITE)
                rbParBreakNo.setTextColor(Color.BLACK)
            } else {
                rbParBreakYes.setTextColor(Color.BLACK)
                rbParBreakNo.setTextColor(Color.WHITE)
            }
        }
        rgService.setOnCheckedChangeListener { radioGroup, i ->
            val rb = view.findViewById(i) as RadioButton
            if (rb.text.equals("Yes")) {
                rbServiceYes.setTextColor(Color.WHITE)
                rbServiceNo.setTextColor(Color.BLACK)
            } else {
                rbServiceYes.setTextColor(Color.BLACK)
                rbServiceNo.setTextColor(Color.WHITE)
            }
        }
        rgLighting.setOnCheckedChangeListener { radioGroup, i ->
            val rb = view.findViewById(i) as RadioButton
            if (rb.text.equals("Yes")) {
                rbLightingYes.setTextColor(Color.WHITE)
                rbLightingNo.setTextColor(Color.BLACK)
            } else {
                rbLightingYes.setTextColor(Color.BLACK)
                rbLightingNo.setTextColor(Color.WHITE)
            }
        }
        rgOilBelts.setOnCheckedChangeListener { radioGroup, i ->
            val rb = view.findViewById(i) as RadioButton
            if (rb.text.equals("Yes")) {
                rbOilYes.setTextColor(Color.WHITE)
                rbOilNo.setTextColor(Color.BLACK)
            } else {
                rbOilYes.setTextColor(Color.BLACK)
                rbOilNo.setTextColor(Color.WHITE)
            }
        }


        btnSave.setOnClickListener {
            dashboardViewModel.callGetSendInspectionApi(
                scheduleId,
                vehicleId,
                checkedInspectionList,
                "Start"
            )
            //mainFragmentListener.drawerOpen()
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

        //get inspection list
        dashboardViewModel.responseInspectionData.observe(requireActivity()) { ResponseLogin: WebResponse<JsonArray> ->
            if (ResponseLogin.status == Status.SUCCESS) {
                val jsonArray: JsonArray = ResponseLogin.data!!
                inspectionList = arrayListOf()
                checkedInspectionList = hashMapOf()
                for (i in 0 until jsonArray.size()) {
                    val json_data: JsonObject = jsonArray[i].asJsonObject
                    val inspectionJson = InspectionJson(
                        json_data.get("name").asString,
                        json_data.get("slug").asString
                    )
                    inspectionList.add(inspectionJson)
                    checkedInspectionList[json_data.get("slug").asString] = "0"
                }
                initRV()

            }
            if (ResponseLogin.status == Status.FAILURE) {
                Toast.makeText(requireActivity(), ResponseLogin.errorMsg, Toast.LENGTH_SHORT)
                    .show()
            }

        }

        // post inspection list
        dashboardViewModel.responseInspectionReturnData.observe(requireActivity()) { ResponseLogin: WebResponse<DefaultModel> ->
            if (ResponseLogin.status == Status.SUCCESS) {
                Toast.makeText(requireActivity(), ResponseLogin.errorMsg, Toast.LENGTH_SHORT)
                    .show()
                mainFragmentListener.backTopMostFragment()
            }
            if (ResponseLogin.status == Status.FAILURE) {
                Toast.makeText(requireActivity(), ResponseLogin.errorMsg, Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun initRV() {
        rvInspectionList.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        inspectionListAdapter = InspectionListAdapter(inspectionList, requireActivity(),
            object : InspectionListAdapter.OnItemCheckListener {
                override fun onItemView(id: String?, value: String?) {
                    checkedInspectionList[id] = value!!
                }

                override fun onItemEdit(id: String?) {
                    TODO("Not yet implemented")
                }

                override fun onItemDelete(id: String?) {
                    TODO("Not yet implemented")
                }
            }
        )
        rvInspectionList.adapter = inspectionListAdapter
    }

}