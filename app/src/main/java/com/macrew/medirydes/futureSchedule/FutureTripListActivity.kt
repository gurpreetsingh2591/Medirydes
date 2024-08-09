package com.macrew.medirydes.futureSchedule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Constants
import com.macrew.medirydes.annotation.FragmentType
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.future.FutureModel
import com.macrew.medirydes.dashboard.model.future.Trips
import com.macrew.medirydes.dashboard.view.activities.NotificationActivity
import com.macrew.medirydes.dashboard.view.activities.ProfileActivity
import com.macrew.medirydes.dashboard.view.adapters.TripSequenceAdapter
import com.macrew.medirydes.dashboard.viewModel.DashboardViewModel
import com.macrew.medirydes.interfaces.MainFragmentListener
import com.macrew.medirydes.retrofit.WebResponse
import com.macrew.medirydes.tripDetail.view.ScheduleTripDetailActivity
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.macrew.medirydes.utils.Static
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_future_trip_list.*
import kotlinx.android.synthetic.main.activity_future_trip_list.btnSave
import kotlinx.android.synthetic.main.activity_future_trip_list.llNote
import kotlinx.android.synthetic.main.activity_future_trip_list.llReArrange
import kotlinx.android.synthetic.main.activity_future_trip_list.rbTripReroute
import kotlinx.android.synthetic.main.activity_future_trip_list.rbTripSequence
import kotlinx.android.synthetic.main.activity_future_trip_list.rgTripFilter
import kotlinx.android.synthetic.main.activity_future_trip_list.rvReArrangeTripList
import kotlinx.android.synthetic.main.activity_future_trip_list.rvTripList
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.tab_bar.*

class FutureTripListActivity : AppCompatActivity() {
    val static = Static()
    private lateinit var dashboardViewModel: DashboardViewModel
    var tripList: ArrayList<Trips?> = ArrayList()
    private lateinit var tripSequenceAdapter: TripSequenceAdapter
    private lateinit var futureReArrangeTripListAdapter: FutureReArrangeTripListAdapter
    var date = ""
    var day = ""
    var endTime = ""
    var startTime = ""
    var tripIdsList: ArrayList<String> = arrayListOf()
    private lateinit var futureTripsListAdapter: FutureTripsListAdapter

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
            val intent = Intent(activity, FutureTripListActivity::class.java)
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
        setContentView(R.layout.activity_future_trip_list)


        initObserver()
        tripIdsList = arrayListOf()

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
        initRVReArrange()


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

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        imgBack.visibility = View.VISIBLE
        imgNavigation.visibility = View.GONE

        if (SharedPrefrencesUtils.getUserImage() != "") {
            Glide.with(this).load(SharedPrefrencesUtils.getUserImage()).into(imgProfile);
           // Picasso.get().load(SharedPrefrencesUtils.getUserImage()).into(imgProfile)
        }
        imgBack.setOnClickListener {
            finish()
        }
        imgNotification.setOnClickListener {
            //imgProfile.visibility = View.VISIBLE
            NotificationActivity.startActivity(this, "")
        }

        imgProfile.setOnClickListener {
            // imgProfile.visibility = View.VISIBLE
            ProfileActivity.startActivity(this, "")
        }
        btnSave.setOnClickListener {
            dashboardViewModel.callGetSendReArrangeListFutureApi(tripIdsList)
        }

        rgTripFilter.setOnCheckedChangeListener { _, i ->
            val rb = findViewById<RadioButton>(i)
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
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        futureTripsListAdapter = FutureTripsListAdapter(tripList, this,
            object : FutureTripsListAdapter.OnItemCheckListener {
                override fun onItemView(id: String?, pos: Int) {

                    FutureTripDetailActivity.startActivity(
                        this@FutureTripListActivity,
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
        rvTripList.adapter = futureTripsListAdapter
    }

    private fun initRVReArrange() {
        tripIdsList = arrayListOf()
        for (i in tripList.indices) {
            tripIdsList.add(tripList[i]?.id.toString())
        }

        rvReArrangeTripList.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        futureReArrangeTripListAdapter = FutureReArrangeTripListAdapter(tripList, this,
            object : FutureReArrangeTripListAdapter.OnItemCheckListener {
                override fun onItemView(id: String?, pos: Int) {
                    FutureTripDetailActivity.startActivity(
                        this@FutureTripListActivity,
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
        rvReArrangeTripList.adapter = futureReArrangeTripListAdapter
    }

    //initialise Observer
    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        dashboardViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                progressLoading.visibility = View.VISIBLE
            else
                progressLoading.visibility = View.GONE
        }

        dashboardViewModel.responseReArrangeListFutureData.observe(
            this
        ) { ResponseLogin: WebResponse<FutureModel> ->
            if (ResponseLogin.status == Status.SUCCESS) {

                if (ResponseLogin.data?.result?.schedules?.size != 0) {
                    tripList = ResponseLogin.data?.result?.schedules?.get(0)?.trips!!
                    tripSequenceAdapter.notifyDataSetChanged()
                    futureReArrangeTripListAdapter.notifyDataSetChanged()
                    for (i in tripList.indices) {
                        tripIdsList = arrayListOf()
                        tripIdsList.add(tripList[i]?.id.toString())
                    }
                }
            }else{
                Toast.makeText(this, "Single trip will no re-arrange", Toast.LENGTH_SHORT)
                    .show()
            }
            if (ResponseLogin.status == Status.FAILURE) {
                Toast.makeText(this, ResponseLogin.errorMsg, Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }
}