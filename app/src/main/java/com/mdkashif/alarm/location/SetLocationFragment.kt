package com.mdkashif.alarm.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Fragment
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.mdkashif.alarm.R
import kotlinx.android.synthetic.main.fragment_add_location.*
import kotlinx.android.synthetic.main.fragment_add_location.view.*

class SetLocationFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private val PLAY_SERVICES_RESOLUTION_REQUEST = 1000
    private val UPDATE_INTERVAL = 10000
    private val FATEST_INTERVAL = 1000
    private var DISPLACEMENT = 1000

    private var mgoogleMap: GoogleMap? = null
    private var mLastLocation: Location? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null

    private var mapView : MapView? = null
    private var latitude: Double = 0.toDouble()
    private var longitude:Double = 0.toDouble()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_add_location, container, false)
        val behavior = BottomSheetBehavior.from(view.bottom_sheet)
        mapView = view.findViewById(R.id.map)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                fab_add.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start()
            }
        })

        view.places_autocomplete.setOnPlaceSelectedListener {
            // do something awesome with the selected place
        }

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mapView != null) {
            mapView!!.onCreate(null)
            mapView!!.onResume()
            mapView!!.getMapAsync(this)
        }

        if (checkPlayServices()) {

            buildGoogleApiClient()

            createLocationRequest()
        }
    }

    @Synchronized
    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL.toLong()
        mLocationRequest!!.fastestInterval = FATEST_INTERVAL.toLong()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.smallestDisplacement = DISPLACEMENT.toFloat()
    }

    private fun checkPlayServices(): Boolean {
        val resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(activity)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show()
            } else {
                Toast.makeText(activity,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show()
            }
            return false
        }
        return true
    }

    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this)

    }

    private fun stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this)
    }

    private fun displayLocation() {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient)

        if (mLastLocation != null) {
            latitude = mLastLocation!!.latitude
            longitude = mLastLocation!!.longitude
            mapView!!.getMapAsync(this)
        } else
            startLocationUpdates()
    }

    override fun onStart() {
        mGoogleApiClient!!.connect()
        super.onStart()
        if (mGoogleApiClient!!.isConnected) {
            startLocationUpdates()
        }
    }

    override fun onResume() {
        mGoogleApiClient!!.connect()
        mapView!!.onResume()
        checkPlayServices()

        if (mGoogleApiClient!!.isConnected) {
            startLocationUpdates()
        }
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
        stopLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        mgoogleMap = googleMap
        mgoogleMap!!.uiSettings.isMapToolbarEnabled = false
        mgoogleMap!!.uiSettings.isRotateGesturesEnabled = false
        mgoogleMap!!.uiSettings.isCompassEnabled = false
        mgoogleMap!!.uiSettings.isMapToolbarEnabled = false
        mgoogleMap!!.uiSettings.isTiltGesturesEnabled = false
        mgoogleMap!!.isMyLocationEnabled = true
        mgoogleMap!!.uiSettings.isMyLocationButtonEnabled = false

        val pos = LatLng(latitude, longitude)

        try {
            val success = mgoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_style))
            if (!success) {
            }
        } catch (e: Resources.NotFoundException) {
        }

        mgoogleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 18f))


    }

    override fun onConnected(p0: Bundle?) {
        displayLocation()
        startLocationUpdates()
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(location: Location?) {
        mLastLocation = location

        displayLocation()
    }

}
