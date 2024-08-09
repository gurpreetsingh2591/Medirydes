package com.macrew.medirydes.dashboard.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.reports.ReportsModel
import com.macrew.medirydes.dashboard.model.reports.ScheduleData
import com.macrew.medirydes.dashboard.view.adapters.ReportListAdapter
import com.macrew.medirydes.dashboard.viewModel.DashboardViewModel
import com.macrew.medirydes.interfaces.MainFragmentListener
import com.macrew.medirydes.reports.view.ReportTripListActivity
import com.macrew.medirydes.retrofit.WebResponse
import com.macrew.medirydes.utils.DateTimePicker
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.fragment_reports.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReportsFragment : Fragment() {
    val static = Static()
    private lateinit var mainFragmentListener: MainFragmentListener
    private lateinit var reportListAdapter: ReportListAdapter
    private lateinit var reportList: ArrayList<ScheduleData?>
    private lateinit var dashboardViewModel: DashboardViewModel
    private var fromDate = ""
    private var toDate = ""

    companion object {
        private val bundle = Bundle()

        private var ARG_PARAM = "currencyList"

        fun getInstance(): Fragment {
            return ReportsFragment()
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
        return inflater.inflate(R.layout.fragment_reports, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // initObserver()
        // dashboardViewModel.callGetDashboardApi()
        val args = arguments
        initObserver()
        dashboardViewModel.callGetReportListApi("", "")
        initUI()

    }

    @SuppressLint("SimpleDateFormat")
    private fun initUI() {

        fromDate = static.getCurrentDate()
        toDate = static.getCurrentDate()

        txtFromDate.setOnClickListener {
            DateTimePicker(requireActivity()) {
                   val sdf = SimpleDateFormat(DateTimePicker.getFormat("da"), Locale.getDefault())
                   txtFromDate.text = sdf.format(it.calendar.time)
                   fromDate = sdf.format(it.calendar.time)

               }.show()

        }

        txtToDate.setOnClickListener {
            DateTimePicker(requireActivity()) {
                val sdf = SimpleDateFormat(DateTimePicker.getFormat("da"), Locale.getDefault())
                txtToDate.text = sdf.format(it.calendar.time)
                toDate = sdf.format(it.calendar.time)
                var fDate = Date()
                var tDate = Date()
                try {
                    fDate = SimpleDateFormat("yyyy-MM-dd").parse(fromDate)!!
                    tDate = SimpleDateFormat("yyyy-MM-dd").parse(toDate)!!
                } catch (e: ParseException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

                if (fromDate == "" && toDate == "") {
                    Toast.makeText(
                        requireActivity(),
                        "Please Enter the both dates",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (tDate.before(fDate))
                    Toast.makeText(
                        requireActivity(),
                        "Please Enter grater date in to_date ",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                else {
                    dashboardViewModel.callGetReportListApi(fromDate, toDate)
                }
            }.show()
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

        dashboardViewModel.responseReportsData.observe(
            requireActivity()
        ) { ResponseLogin: WebResponse<ReportsModel> ->
            if (ResponseLogin.status == Status.SUCCESS) {
                reportList = ArrayList()
                reportList = ResponseLogin.data?.result!!.schedules
                initRV()
                //Check report list
                if (reportList.size == 0) {
                    txtNoList.visibility = View.VISIBLE
                } else {
                    txtNoList.visibility = View.GONE
                }
            }
            if (ResponseLogin.status == Status.FAILURE) {
                Toast.makeText(requireActivity(), ResponseLogin.errorMsg, Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }

    private fun initRV() {

        if (reportList.size != 0) {
            rvReports.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            reportListAdapter = ReportListAdapter(reportList, requireActivity(),
                object : ReportListAdapter.OnItemCheckListener {
                    override fun onItemView(id: String?, pos: Int) {

                        if (reportList[pos]?.trips?.size!=0) {
                            ReportTripListActivity.startActivity(
                                activity,
                                reportList[pos]?.trips!!,
                                reportList[pos]?.date!!,
                                reportList[pos]?.day!!,
                                reportList[pos]?.start_time!!,
                                reportList[pos]?.end_time!!
                            )

                            SharedPrefrencesUtils.setMobilityType(reportList[pos]?.vehicle?.name!!)
                        }else{
                            Toast.makeText(requireActivity(), "Trips are Not Available", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                    override fun onItemEdit(id: String?) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemDelete(id: String?) {
                        TODO("Not yet implemented")
                    }
                }
            )
            rvReports.adapter = reportListAdapter
        }
    }
}