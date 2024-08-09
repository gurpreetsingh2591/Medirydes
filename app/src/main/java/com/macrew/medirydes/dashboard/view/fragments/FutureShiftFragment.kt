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
import com.macrew.medirydes.dashboard.model.future.FutureModel
import com.macrew.medirydes.dashboard.model.future.ScheduleData

import com.macrew.medirydes.dashboard.view.adapters.FutureShiftAdapter
import com.macrew.medirydes.dashboard.viewModel.DashboardViewModel
import com.macrew.medirydes.futureSchedule.FutureTripListActivity
import com.macrew.medirydes.interfaces.MainFragmentListener
import com.macrew.medirydes.retrofit.WebResponse
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.fragment_future_shift.*


class FutureShiftFragment : Fragment() {
    val static = Static()
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var mainFragmentListener: MainFragmentListener
    private lateinit var futureShiftAdapter: FutureShiftAdapter
    private lateinit var scheduleList: ArrayList<ScheduleData?>


    companion object {
        private val bundle = Bundle()
        private var ARG_PARAM = "currencyList"
        fun getInstance(): Fragment {
            return FutureShiftFragment()
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
        return inflater.inflate(R.layout.fragment_future_shift, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        initObserver()
        dashboardViewModel.callGetFutureListApi()
        initUI()
    }

    private fun initUI() {

    }


    private fun initRV() {

        rvFuture.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        futureShiftAdapter = FutureShiftAdapter(scheduleList, requireActivity(),
            object : FutureShiftAdapter.OnItemCheckListener {
                override fun onItemView(id: String?,pos:Int) {
                  //  mainFragmentListener.showFragment(FragmentType.TRIP_DETAIL_FRAGMENT, "")
                    if ( scheduleList[pos]?.trips!!.size!=0) {
                        FutureTripListActivity.startActivity(
                            activity,
                            scheduleList[pos]?.trips!!,
                            scheduleList[pos]?.date!!,
                            scheduleList[pos]?.day!!,
                            scheduleList[pos]?.start_time!!,
                            scheduleList[pos]?.end_time!!
                        )
                        SharedPrefrencesUtils.setMobilityType(scheduleList[pos]?.vehicle?.name!!)
                    }else{
                        Toast.makeText(requireActivity(),"Trips are Not Available", Toast.LENGTH_SHORT)
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
        rvFuture.adapter = futureShiftAdapter
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

        dashboardViewModel.responseFutureData.observe(
            requireActivity()
        ) { ResponseLogin: WebResponse<FutureModel> ->
            if (ResponseLogin.status == Status.SUCCESS) {
                scheduleList= arrayListOf()
                scheduleList = ResponseLogin.data?.result?.schedules!!


                initRV()
                if (scheduleList.size != 0) {
                    txtNoList.visibility=View.GONE
                    rvFuture.visibility=View.VISIBLE
                }else{
                    txtNoList.visibility=View.VISIBLE
                    rvFuture.visibility=View.GONE

                }
            }
            if (ResponseLogin.status == Status.FAILURE) {
                Toast.makeText(requireActivity(), ResponseLogin.errorMsg, Toast.LENGTH_SHORT)
                    .show()
            }

        }
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        dashboardViewModel.isLoading.observe(requireActivity()) { aBoolean ->
            if (aBoolean)
                progressLoading!!.visibility = View.VISIBLE
            else
                progressLoading!!.visibility =
                    View.GONE
        }
    }


}