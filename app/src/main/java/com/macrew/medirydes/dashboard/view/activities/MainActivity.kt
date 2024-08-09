package com.macrew.medirydes.dashboard.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.view.TextureView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.FragmentType
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.DefaultModel
import com.macrew.medirydes.dashboard.model.currentSchedule.CurrentScheduleModel
import com.macrew.medirydes.dashboard.model.currentSchedule.ScheduleData
import com.macrew.medirydes.dashboard.model.currentSchedule.Trips
import com.macrew.medirydes.dashboard.view.fragments.*
import com.macrew.medirydes.dashboard.viewModel.DashboardViewModel
import com.macrew.medirydes.interfaces.MainFragmentListener
import com.macrew.medirydes.login.view.LoginActivity
import com.macrew.medirydes.retrofit.WebResponse
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.navigation_drawer_items.*
import kotlinx.android.synthetic.main.navigation_drawer_items.txtCompanyName
import kotlinx.android.synthetic.main.navigation_drawer_items.txtName
import kotlinx.android.synthetic.main.tab_bar.*

class MainActivity : AppCompatActivity(), MainFragmentListener {
    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private lateinit var scheduleData: ArrayList<ScheduleData?>
    private var serverTime = ""
    private lateinit var trips: ArrayList<Trips?>
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 30
        fastestInterval = 10
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 60
    }
    private lateinit var dashboardViewModel: DashboardViewModel

    companion object {
        lateinit var imageViewProfile: CircleImageView
        lateinit var imageTabProfile: CircleImageView
        lateinit var txtTabName: TextView


        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99

        fun startActivity(activity: Activity?, screen: String) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("screen", screen)
            activity?.startActivity(intent)
            activity?.finishAffinity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()
        initObserver()
        dashboardViewModel.callGetDashboardApi()
        initUI()
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //The last location in the list is the newest
                val location = locationList.last()

                SharedPrefrencesUtils.setCurrentLat(location.latitude.toString())
                SharedPrefrencesUtils.setCurrentLong(location.longitude.toString())
//                Toast.makeText(
//                    this@MainActivity,
//                    "Lat: " +location.latitude.toString()+"  Long: "+location.longitude.toString(),
//                    Toast.LENGTH_SHORT)
//                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProvider?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationProvider?.removeLocationUpdates(locationCallback)
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    private fun initUI() {
        imageViewProfile = findViewById(R.id.imgProfile)
        imageTabProfile = findViewById(R.id.img_profile)
        txtTabName = findViewById(R.id.txtName)
        if (!SharedPrefrencesUtils.getUserImage().equals("")) {
            Glide.with(this).load(SharedPrefrencesUtils.getUserImage()).into(imgProfile);
            Glide.with(this).load(SharedPrefrencesUtils.getUserImage()).into(img_profile);
          //  Picasso.get().load(SharedPrefrencesUtils.getUserImage()).into(imgProfile)
          //  Picasso.get().load(SharedPrefrencesUtils.getUserImage()).into(img_profile)
        }

        btnEnd.setOnClickListener {
            if (scheduleData[0]!!.status.equals("started")) {
                dashboardViewModel.callGetStartScheduleApi(
                    scheduleData[0]!!.id.toString(),
                    "completed"
                )
            }
        }
        txtCompanyName.text=SharedPrefrencesUtils.getCompanyName()
        txtName.text = SharedPrefrencesUtils.getUserName()
        llLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
            SharedPrefrencesUtils.clearUser()
        }
        imgClose.setOnClickListener {
            closeDrawer()
        }

        imgNavigation.setOnClickListener {
            openDrawer()
        }
        imgNotification.setOnClickListener {
            closeDrawer()
            NotificationActivity.startActivity(this@MainActivity, "")

        }
        imgProfile.setOnClickListener {
            closeDrawer()
            ProfileActivity.startActivity(this@MainActivity, "")
            //  showFragment(FragmentType.PROFILE_FRAGMENT, "")

        }

        llHome.setOnClickListener {
            object : CountDownTimer(200, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {
                    closeDrawer()
                    showFragment(FragmentType.HOME_FRAGMENT, "")
                }
            }.start()

        }
        llHistory.setOnClickListener {

            object : CountDownTimer(200, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {
                    closeDrawer()
                    showFragment(FragmentType.REPORTS_FRAGMENT, "")
                }
            }.start()

        }
        llCurrent.setOnClickListener {

            object : CountDownTimer(200, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {

                    closeDrawer()
                    showFragment(FragmentType.CURRENT_SHIFT_FRAGMENT, "")

                }
            }.start()

        }
        llFuture.setOnClickListener {
            object : CountDownTimer(200, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {
                    closeDrawer()
                    showFragment(FragmentType.FUTURE_SHIFT_FRAGMENT, "")
                }
            }.start()

        }
        llRequestTime.setOnClickListener {
            object : CountDownTimer(200, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {
                    closeDrawer()
                    showFragment(FragmentType.REQUEST_TIME_FRAGMENT, "")
                }
            }.start()

        }
        llProfile.setOnClickListener {
            object : CountDownTimer(200, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {
                    closeDrawer()
                    ProfileActivity.startActivity(this@MainActivity, "")
                    //  showFragment(FragmentType.PROFILE_FRAGMENT, "")
                }
            }.start()

        }
        llFuel.setOnClickListener {
            object : CountDownTimer(200, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {
                    closeDrawer()
                    showFragment(FragmentType.FUEL_FRAGMENT, "")
                }
            }.start()
        }

        llOrders.setOnClickListener {
            object : CountDownTimer(200, 300) {
                override fun onTick(p0: Long) {
                }

                override fun onFinish() {
                    closeDrawer()
                    showFragment(FragmentType.ORDER_FRAGMENT, "")
                }
            }.start()
        }
    }

    private fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START, false)
    }


    private fun openDrawer() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun showFragment(tag: String, payload: Any?) {
        val bundle = Bundle()
        imgProfile.visibility = View.VISIBLE
        when (tag) {
            FragmentType.HOME_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        setCustomAnimations(
                            R.anim.fadein,
                            R.anim.fadeout
                        )
                        val replace = replace(
                            R.id.fragment_container_view,
                            HomeFragment.getInstance(scheduleData)
                        )
                        addToBackStack(FragmentType.HOME_FRAGMENT)
                        commit()
                    }

            }

            FragmentType.CURRENT_SHIFT_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {

                        setCustomAnimations(
                            R.anim.fadein,
                            R.anim.fadeout
                        )
                        replace(
                            R.id.fragment_container_view,
                            CurrentScheduleFragment.getInstance(scheduleData, serverTime)
                        )

                        addToBackStack(FragmentType.CURRENT_SHIFT_FRAGMENT)
                        commit()
                    }

            }
            FragmentType.FUTURE_SHIFT_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(
                            R.id.fragment_container_view,
                            FutureShiftFragment.getInstance()
                        )
                        setCustomAnimations(
                            R.anim.fadein,
                            R.anim.fadeout
                        )

                        addToBackStack(FragmentType.FUTURE_SHIFT_FRAGMENT)
                        commit()
                    }

            }
            FragmentType.REQUEST_TIME_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(
                            R.id.fragment_container_view,
                            RequestTimeFragment.getInstance()
                        )
                        setCustomAnimations(
                            R.anim.fadein,
                            R.anim.fadeout
                        )

                        addToBackStack(FragmentType.REQUEST_TIME_FRAGMENT)
                        commit()
                    }


            }

            FragmentType.REPORTS_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(
                            R.id.fragment_container_view,
                            ReportsFragment.getInstance()
                        )
                        setCustomAnimations(
                            R.anim.fadein,
                            R.anim.fadeout
                        )

                        addToBackStack(FragmentType.REPORTS_FRAGMENT)
                        commit()
                    }

            }


//            FragmentType.PROFILE_FRAGMENT -> {
//                imgProfile.visibility = View.GONE
//                supportFragmentManager.beginTransaction()
//                    .apply {
//                        replace(
//                            R.id.fragment_container_view,
//                            ProfileFragments.getInstance()
//                        )
//                        setCustomAnimations(
//                            R.anim.fadein,
//                            R.anim.fadeout
//                        )
//                        addToBackStack(FragmentType.PROFILE_FRAGMENT)
//                        commit()
//                    }
//            }
            FragmentType.FUEL_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(
                            R.id.fragment_container_view,
                            FuelFragment.getInstance()
                        )
                        setCustomAnimations(
                            R.anim.fadein,
                            R.anim.fadeout
                        )
                        addToBackStack(FragmentType.FUEL_FRAGMENT)
                        commit()
                    }
            }
            FragmentType.INSPECTION_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(
                            R.id.fragment_container_view,
                            InspectionFragment.getInstance(
                                scheduleData[0]!!.id.toString(),
                                scheduleData[0]!!.vehicle_id.toString()
                            )
                        )
                        setCustomAnimations(
                            R.anim.fadein,
                            R.anim.fadeout
                        )
                        addToBackStack(FragmentType.INSPECTION_FRAGMENT)
                        commit()

                    }
            }

            FragmentType.TRIP_DETAIL_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(
                            R.id.fragment_container_view,

                            TripDetailFragment.getInstance(
                                trips,
                                scheduleData[0]?.date!!,
                                scheduleData[0]?.day!!,
                                scheduleData[0]?.start_time!!,
                                scheduleData[0]?.end_time!!
                            )
                        )
                        setCustomAnimations(
                            R.anim.fadein,
                            R.anim.fadeout
                        )
                        addToBackStack(FragmentType.TRIP_DETAIL_FRAGMENT)
                        commit()
                    }
            }


            FragmentType.ORDER_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(
                            R.id.fragment_container_view,
                            OrdersFragment.getInstance()
                        )
                        setCustomAnimations(
                            R.anim.fadein,
                            R.anim.fadeout
                        )
                        addToBackStack(FragmentType.ORDER_FRAGMENT)
                        commit()
                    }
            }


        }

    }

    override fun popTillFragment(tag: String, flag: Int) {
        when (tag) {
            FragmentType.HOME_FRAGMENT -> {
                onBackPressed()
            }
            else -> supportFragmentManager.popBackStack(tag, flag)
        }
    }

    override fun popTopMostFragment() {
        onBackPressed()
    }

    override fun backTopMostFragment() {
        supportFragmentManager.popBackStack()
    }

    override fun drawerOpen() {
        openDrawer()
        llButton.visibility = View.VISIBLE
    }

    /*Back Pressed*/
    override fun onBackPressed() {

//        val fragment =
//            this.supportFragmentManager.findFragmentById(R.id.fragment_container_view)
//        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
//            super.onBackPressed()
//        }

        val fr: Fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) ?: return
        when (fr.javaClass.simpleName) {
            FragmentType.HOME_FRAGMENT -> {
                finish()
            }
            else -> {
                backTopMostFragment()
            }
        }

        if (fr.javaClass.simpleName.equals(FragmentType.PROFILE_FRAGMENT)) {
            imgProfile.visibility = View.GONE
        } else {
            imgProfile.visibility = View.VISIBLE
        }

        if (fr.javaClass.simpleName.equals(FragmentType.NOTIFICATION_FRAGMENT)) {
            imgNotification.visibility = View.GONE
        } else {
            imgNotification.visibility = View.VISIBLE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProvider?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
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

        dashboardViewModel.responseDashboardData.observe(
            this,
            { ResponseLogin: WebResponse<CurrentScheduleModel> ->
                if (ResponseLogin.status == Status.SUCCESS) {
                    if (ResponseLogin.data?.result?.schedules?.size != 0) {
                        scheduleData = arrayListOf()
                        trips = arrayListOf()
                        scheduleData = ResponseLogin.data?.result?.schedules!!
                        trips = scheduleData[0]!!.trips
                        serverTime = ResponseLogin.data?.result?.server_time!!
                        SharedPrefrencesUtils.setMobilityType(ResponseLogin.data?.result?.schedules!![0]?.vehicle?.name!!)

                    } else {
                        trips = arrayListOf()
                        scheduleData = ArrayList()
                    }
                    //Load first fragment
                    showFragment(FragmentType.HOME_FRAGMENT, "")
                    if (scheduleData.size != 0) {
                        if (scheduleData[0]!!.status.equals("started")) {
                            llButton.visibility = View.VISIBLE
                            btnStartShift.text =
                                "Ongoing shift " + scheduleData[0]?.start_time + " - " + scheduleData[0]?.end_time
                        }
                    }
                }
                if (ResponseLogin.status == Status.FAILURE) {
                    Toast.makeText(this, ResponseLogin.errorMsg, Toast.LENGTH_SHORT)
                        .show()
                    scheduleData = ArrayList()
                    //Load first fragment
                    showFragment(FragmentType.HOME_FRAGMENT, "")

                }

            }
        )

        //End  schedule response
        dashboardViewModel.responseStartScheduleData.observe(
            this
        ) { ResponseLogin: WebResponse<CurrentScheduleModel> ->
            if (ResponseLogin.status == Status.SUCCESS) {
                dashboardViewModel.callGetDashboardApi()
                closeDrawer()
                Toast.makeText(this, ResponseLogin.errorMsg, Toast.LENGTH_SHORT).show()
            }
            if (ResponseLogin.status == Status.FAILURE) {
                Toast.makeText(this, ResponseLogin.errorMsg, Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            if (null != fragment) {
                fragment.onActivityResult(requestCode, resultCode, data)
            } else {
                ProfileFragments().onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}