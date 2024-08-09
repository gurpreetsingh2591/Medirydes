package com.macrew.medirydes.dashboard.view.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Constants
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.currentSchedule.CurrentScheduleModel
import com.macrew.medirydes.dashboard.model.currentSchedule.Trips
import com.macrew.medirydes.dashboard.model.future.FutureModel
import com.macrew.medirydes.dashboard.view.adapters.ReArrangeTripListAdapter
import com.macrew.medirydes.dashboard.view.adapters.TripSequenceAdapter
import com.macrew.medirydes.dashboard.viewModel.DashboardViewModel
import com.macrew.medirydes.retrofit.WebResponse
import com.macrew.medirydes.tripDetail.view.ScheduleTripDetailActivity
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_trip_detail.*
import java.text.SimpleDateFormat

class TripDetailFragment : Fragment() {
    val static = Static()
    private lateinit var dashboardViewModel: DashboardViewModel
    var tripList: ArrayList<Trips?> = ArrayList()
    private lateinit var tripSequenceAdapter: TripSequenceAdapter
    private lateinit var reArrangeTripListAdapter: ReArrangeTripListAdapter
    var date = ""
    var day = ""
    var endTime = ""
    var startTime = ""
    var tripIdsList: ArrayList<String> = arrayListOf()

    companion object {
        private val bundle = Bundle()

        fun getInstance(
            trips: ArrayList<Trips?>,
            date: String,
            day: String,
            startTime: String,
            endTime: String
        ): Fragment {
            var tripDetailActivity = TripDetailFragment()
            bundle.putParcelableArrayList(Constants.ARG_PARAM_TRIP_LIST, trips)
            bundle.putString(Constants.ARG_PARAM_DATE, date)
            bundle.putString(Constants.ARG_PARAM_DAY, day)
            bundle.putString(Constants.ARG_PARAM_START_TIME, startTime)
            bundle.putString(Constants.ARG_PARAM_END_TIME, endTime)
            tripDetailActivity.arguments = bundle
            return tripDetailActivity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trip_detail, container, false)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObserver()
        tripIdsList = arrayListOf()
        val args = arguments
        tripList = args?.getParcelableArrayList(Constants.ARG_PARAM_TRIP_LIST)!!
        date = args.getString(Constants.ARG_PARAM_DATE)!!
        day = args.getString(Constants.ARG_PARAM_DAY)!!
        startTime = args.getString(Constants.ARG_PARAM_START_TIME)!!
        endTime = args.getString(Constants.ARG_PARAM_END_TIME)!!
        initUI(view)
        initRV()
        initRVReArrange()
        txtDateDay.text =  static.dateFormatString(date) + " " + static.capitalizeString(day) + ", "
        txtStartEndTime.text = "$startTime - $endTime"

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun isLongPressDragEnabled() = true
            override fun isItemViewSwipeEnabled() = false

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags =
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                val swipeFlags =
                    if (isItemViewSwipeEnabled) ItemTouchHelper.START or ItemTouchHelper.END else 0
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                if (viewHolder.itemViewType != target.itemViewType)
                    return false
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                val item = tripList.removeAt(fromPosition)
                tripList.add(toPosition, item)
                recyclerView.adapter!!.notifyItemMoved(fromPosition, toPosition)
                tripIdsList.clear()
                for (i in tripList.indices) {
                    tripIdsList.add(tripList[i]?.id.toString())
                }

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                tripList.removeAt(position)
                rvReArrangeTripList.adapter!!.notifyItemRemoved(position)
            }

        })
        itemTouchHelper.attachToRecyclerView(rvReArrangeTripList)
    }


    private fun initUI(view: View) {

        btnSave.setOnClickListener {
            dashboardViewModel.callGetSendReArrangeListApi(tripIdsList)
        }

        rgTripFilter.setOnCheckedChangeListener { radioGroup, i ->

            val rb = view.findViewById(i) as RadioButton
            if (rb.text.equals("Reroute Trips")) {
                rbTripReroute.setTextColor(Color.WHITE)
                rbTripSequence.setTextColor(Color.BLACK)
            } else {
                rbTripReroute.setTextColor(Color.BLACK)
                rbTripSequence.setTextColor(Color.WHITE)
            }
        }


        rbTripSequence.setOnClickListener {
            rvTripList.visibility = View.VISIBLE
            llReArrange.visibility = View.GONE
            llNote.visibility = View.GONE
        }

        rbTripReroute.setOnClickListener {
            rvTripList.visibility = View.GONE
            llReArrange.visibility = View.VISIBLE
            llNote.visibility = View.VISIBLE
        }


    }

    private fun initRV() {

        rvTripList.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        tripSequenceAdapter = TripSequenceAdapter(tripList, requireActivity(),
            object : TripSequenceAdapter.OnItemCheckListener {
                override fun onItemView(id: String?, pos: Int) {
                    //  mainFragmentListener.showFragment(FragmentType.TRIP_DETAIL_FRAGMENT,"")
                    ScheduleTripDetailActivity.startActivity(
                        activity,
                        tripList[pos]!!,
                        date,
                        day,
                        startTime,
                        endTime
                    )
                    //  TripDetailWithMapActivity.startActivity(activity,"")
                }

                override fun onItemEdit(id: String?) {
                    TODO("Not yet implemented")
                }

                override fun onItemDelete(id: String?) {
                    TODO("Not yet implemented")
                }
            }
        )
        rvTripList.adapter = tripSequenceAdapter
    }

    private fun initRVReArrange() {
        tripIdsList = arrayListOf()
        for (i in tripList.indices) {
            tripIdsList.add(tripList[i]?.id.toString())
        }

        rvReArrangeTripList.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        reArrangeTripListAdapter = ReArrangeTripListAdapter(tripList, requireActivity(),
            object : ReArrangeTripListAdapter.OnItemCheckListener {
                override fun onItemView(id: String?, pos: Int) {

                    ScheduleTripDetailActivity.startActivity(
                        activity,
                        tripList[pos]!!,
                        date,
                        day,
                        startTime,
                        endTime
                    )


                    //  TripDetailWithMapActivity.startActivity(activity,"")
                }

                override fun onItemEdit(id: String?) {
                    TODO("Not yet implemented")
                }

                override fun onItemDelete(id: String?) {
                    TODO("Not yet implemented")
                }

                override fun onItemLongPress(id: String?) {
                    TODO("Not yet implemented")
                }
            }
        )
        rvReArrangeTripList.adapter = reArrangeTripListAdapter
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

        dashboardViewModel.responseReArrangeListData.observe(
            requireActivity()
        ) { ResponseLogin: WebResponse<CurrentScheduleModel> ->
            if (ResponseLogin.status == Status.SUCCESS) {
                tripList= ResponseLogin.data?.result?.schedules?.get(0)?.trips!!
                tripSequenceAdapter.notifyDataSetChanged()
                reArrangeTripListAdapter.notifyDataSetChanged()
                for (i in tripList.indices) {
                    tripIdsList = arrayListOf()
                    tripIdsList.add(tripList[i]?.id.toString())
                }
            }
            if (ResponseLogin.status == Status.FAILURE) {
                Toast.makeText(requireActivity(), ResponseLogin.errorMsg, Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }


}