package com.macrew.medirydes.tripDetail.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.directions.route.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.snackbar.Snackbar
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Constants.TAG
import com.macrew.medirydes.dashboard.view.activities.NotificationActivity
import com.macrew.medirydes.dashboard.view.activities.ProfileActivity
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import kotlinx.android.synthetic.main.tab_bar.*
import java.util.*


class TripDetailWithMapActivity : AppCompatActivity(), OnMapReadyCallback, RoutingListener {

    //polyline object
    private var polylines: List<Polyline>? = null
    private var mMap: GoogleMap? = null
    var lat: Double = 0.0
    var long: Double = 0.0
    private lateinit var sourcePosition: LatLng
    private lateinit var destPosition: LatLng

    companion object {
        fun startActivity(activity: Activity?, screen: String) {
            val intent = Intent(activity, TripDetailWithMapActivity::class.java)
            intent.putExtra("screen", screen)
            activity?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_detail_with_map)

        imgBack.visibility = View.VISIBLE
        imgNavigation.visibility = View.GONE
        initUI()
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.api_key))
        }
    }

    private fun initUI() {

        imgBack.visibility = View.VISIBLE
        imgNavigation.visibility = View.GONE
        imgBack.setOnClickListener {
            finish()
        }
        if (SharedPrefrencesUtils.getUserImage() != "") {
            Glide.with(this).load(SharedPrefrencesUtils.getUserImage()).into(imgProfile);
         //   Glide.with(this).load(SharedPrefrencesUtils.getUserImage()).into(img_profile);
        //    Picasso.get().load(SharedPrefrencesUtils.getUserImage()).into(imgProfile)
        }

        imgProfile.setOnClickListener {
            // imgProfile.visibility = View.VISIBLE
            ProfileActivity.startActivity(this, "")
        }
        imgNotification.setOnClickListener {
            //imgProfile.visibility = View.VISIBLE
            NotificationActivity.startActivity(this, "")
        }
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        lat = SharedPrefrencesUtils.getCurrentLat()!!.toDouble()
        long = SharedPrefrencesUtils.getCurrentLong()!!.toDouble()

        val location = LatLng(0.0, 0.0)
        destPosition=location
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        autocompleteFragment?.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        );

        autocompleteFragment!!.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.name + ", " + place.id + "," + place.latLng)
                destPosition = place.latLng!!
                val currentLocation = LatLng(lat, long)
                sourcePosition = currentLocation

                mMap!!.clear()
                findRoutes(sourcePosition, destPosition)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap;

        val currentLoc = LatLng(lat, long)
        // Move the camera instantly to Sydney with a zoom of 15.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15f))
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn())
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10f), 2000, null)

        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        mMap!!.addMarker(MarkerOptions().position(currentLoc).title(""))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(currentLoc))

    }

    // function to find Routes.
    fun findRoutes(Start: LatLng?, End: LatLng?) {
        if (Start == null || End == null) {
            Toast.makeText(this, "Unable to get location", Toast.LENGTH_LONG).show()
        } else {
            val routing = Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(Start, End)
                .key(getString(R.string.api_key)) //also define your api key here.
                .build()
            routing.execute()
        }
    }

    //Routing call back functions.
    override fun onRoutingFailure(e: RouteException) {
        val parentLayout = findViewById<View>(android.R.id.content)
        val snackbar: Snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG)
        snackbar.show()

    }

    override fun onRoutingStart() {
        Toast.makeText(this, "Finding Route...", Toast.LENGTH_LONG).show()
    }

    //If Route finding success..
    override fun onRoutingSuccess(route: ArrayList<Route>, shortestRouteIndex: Int) {
        val center = CameraUpdateFactory.newLatLng(sourcePosition)

        polylines = ArrayList()
        val polyOptions = PolylineOptions()
        var polylineStartLatLng: LatLng? = null
        var polylineEndLatLng: LatLng? = null

        //add route(s) to the map using polyline
        for (i in 0 until route.size) {
            Log.e("route",""+route.size)
            if (i == shortestRouteIndex) {
                polyOptions.color(resources.getColor(R.color.app_base_color))
                polyOptions.width(10f)
                polyOptions.addAll(route[shortestRouteIndex].points)
                val polyline = mMap!!.addPolyline(polyOptions)
                polylineStartLatLng = polyline.points[0]
                val k = polyline.points.size
                polylineEndLatLng = polyline.points[k - 1]
                (polylines as ArrayList<Polyline>).add(polyline)
            } else {
            }
        }

        //Add Marker on route starting position
        val startMarker = MarkerOptions()
        startMarker.position(polylineStartLatLng)
        startMarker.title("My Location")
        mMap!!.addMarker(startMarker)

        //Add Marker on route ending position
        val endMarker = MarkerOptions()
        endMarker.position(polylineEndLatLng)
        endMarker.title("Destination")
        mMap!!.addMarker(endMarker)
    }

    override fun onRoutingCancelled() {
        findRoutes(sourcePosition, destPosition)
    }

    fun onConnectionFailed(connectionResult: ConnectionResult) {
        findRoutes(sourcePosition, destPosition)
    }
}