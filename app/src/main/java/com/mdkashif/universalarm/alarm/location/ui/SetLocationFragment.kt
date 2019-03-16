package com.mdkashif.universalarm.alarm.location.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.location.misc.LocationHelper
import com.mdkashif.universalarm.base.BaseFragment
import com.mdkashif.universalarm.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_set_location.view.*

class SetLocationFragment : BaseFragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mLastLocation: Location
    private lateinit var mGoogleApiClient: GoogleApiClient

    private lateinit var mapView: MapView
    private lateinit var rootView: View

    private val disposable = CompositeDisposable()

    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_set_location, container, false)
        mapView = rootView.findViewById(R.id.map)

        rootView.btSetAlarm.setOnClickListener {
            LocationHelper.setAlarm(mActivity,
                    success = {
                    },
                    failure = {
                        Utils.showToast(it, mActivity)
                    })
        }

        return rootView
    }

//    fun showReminderInMap(context: Context,
//                          map: GoogleMap,
//                          reminder: Reminder) {
//        if (reminder.latLng != null) {
//            val latLng = reminder.latLng as LatLng
//            val vectorToBitmap = vectorToBitmap(context.resources, R.drawable.ic_twotone_location_on_48px)
//            val marker = map.addMarker(MarkerOptions().position(latLng).icon(vectorToBitmap))
//            marker.tag = reminder.id
//            if (reminder.radius != null) {
//                val radius = reminder.radius as Double
//                map.addCircle(CircleOptions()
//                        .center(reminder.latLng)
//                        .radius(radius)
//                        .strokeColor(ContextCompat.getColor(context, R.color.colorAccent))
//                        .fillColor(ContextCompat.getColor(context, R.color.colorReminderFill)))
//            }
//        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mapView != null) {
            mapView.onCreate(null)
            mapView.onResume()
            mapView.getMapAsync(this)
        }

        if (LocationHelper.checkPlayServices(mActivity)) {
            buildGoogleApiClient()
            LocationHelper.createLocationRequest()
        }
    }

    @Synchronized
    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this.context!!)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
    }

    private fun displayLocation() {
        if (ActivityCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient)

        if (mLastLocation != null) {
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
            mapView.getMapAsync(this)
        } else
            startLocationUpdates(this.context!!)
    }

    private fun startLocationUpdates(context: Context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, LocationHelper.mLocationRequest, this)

    }

    private fun stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap!!
        mGoogleMap.uiSettings.isMapToolbarEnabled = false
        mGoogleMap.uiSettings.isRotateGesturesEnabled = false
        mGoogleMap.uiSettings.isCompassEnabled = false
        mGoogleMap.uiSettings.isMapToolbarEnabled = false
        mGoogleMap.uiSettings.isTiltGesturesEnabled = false
        mGoogleMap.isMyLocationEnabled = true
        mGoogleMap.uiSettings.isMyLocationButtonEnabled = false

        val pos = LatLng(latitude, longitude)

        try {
            val success = mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_style))
            if (!success) {
            }
        } catch (e: Resources.NotFoundException) {
        }

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 18f))
        Handler().postDelayed({
            reactAccordingly(pos)
        }, 1500)

        mGoogleMap.setOnMapClickListener {

            mGoogleMap.clear()
            mGoogleMap.addMarker(MarkerOptions().position(it)).setIcon(LocationHelper.getBitmapDescriptorFromVector(mActivity, R.drawable.ic_marker))
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 18f))
            rootView.pbAddress.visibility = View.VISIBLE
            rootView.btSetAlarm.isEnabled = false
            reactAccordingly(it)
            rootView.tvDistance.text = LocationHelper.getDistance(it, pos)
        }
    }

    private fun reactAccordingly(latLng: LatLng) {
        disposable.add(LocationHelper.getAddress(latLng, mActivity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<String>() {
            override fun onComplete() {
                // do nothing
            }

            override fun onNext(t: String) {
                rootView.pbAddress.visibility = View.GONE
                rootView.tvAddress.text = t
                rootView.btSetAlarm.isEnabled = true
            }

            override fun onError(e: Throwable) {
                // do nothing
            }
        }))
    }

    override fun onConnected(p0: Bundle?) {
        displayLocation()
        startLocationUpdates(this.context!!)
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(location: Location?) {
        mLastLocation = location!!
        displayLocation()
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()

        if (mGoogleApiClient.isConnected)
            startLocationUpdates(this.context!!)
    }

    override fun onResume() {
        super.onResume()
        mGoogleApiClient.connect()
        mapView.onResume()
        LocationHelper.checkPlayServices(mActivity)

        if (mGoogleApiClient.isConnected)
            startLocationUpdates(this.context!!)
    }

    override fun onStop() {
        super.onStop()

        if (mGoogleApiClient.isConnected)
            mGoogleApiClient.disconnect()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        stopLocationUpdates()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        disposable.clear()
    }
}
