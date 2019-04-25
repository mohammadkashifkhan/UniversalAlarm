package com.mdkashif.universalarm.alarm.location.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.location.misc.LocationHelper
import com.mdkashif.universalarm.alarm.misc.AlarmOps
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.base.BaseFragment
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.persistence.RoomRepository
import com.mdkashif.universalarm.utils.Utils
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_set_location.*
import kotlinx.android.synthetic.main.fragment_set_location.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SetLocationFragment : BaseFragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener, CoroutineScope {

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var roomRepository: RoomRepository

    @Inject
    lateinit var locationHelper: LocationHelper

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mLastLocation: Location
    private lateinit var mGoogleApiClient: GoogleApiClient

    private lateinit var mapView: MapView
    private lateinit var rootView: View
    lateinit var pos: LatLng

    private lateinit var locationModel: LocationsModel

    private val disposable = CompositeDisposable()

    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_set_location, container, false)
        mapView = rootView.findViewById(R.id.map)

        rootView.btSetAlarm.setOnClickListener(this)
        rootView.btStopAlarm.setOnClickListener(this)

        when {
            arguments != null -> {
                locationModel = arguments!!.getParcelable("editableData") as LocationsModel
                rootView.btSetAlarm.text = "Update Alarm"
                rootView.btStopAlarm.visibility = View.VISIBLE
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locationModel.latitude, locationModel.longitude), 18f))
                reactAccordingly(LatLng(locationModel.latitude, locationModel.longitude))
            }
        }

        return rootView
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btSetAlarm -> {
                when {
                    rootView.btSetAlarm.text != "Update Alarm" -> locationHelper.setAlarm(mActivity, etNote.text.toString(),
                            success = {
                                mActivity.onBackPressed()
                            },
                            failure = {
                                Utils.showToast(it, mActivity)
                            })
                    else -> {
                        locationHelper.updateAlarm(mActivity, etNote.text.toString(),
                                success = {
                                    mActivity.onBackPressed()
                                },
                                failure = {
                                    Utils.showToast(it, mActivity)
                                }, alarmId = locationModel.id)
                    }
                }
            }
            R.id.btStopAlarm -> {
                locationHelper.removeAlarm(locationModel.pIntentRequestCode.toString(), success = {
                    locationModel.status = false
                    launch {
                        roomRepository.amendLocationsAlarmsAsync(AlarmOps.Update.toString(), locationModel)
                    }
                }, failure = {
                    Utils.showToast(it, mActivity)
                }, context = mActivity)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mapView != null) {
            mapView.onCreate(null)
            mapView.onResume()
            mapView.getMapAsync(this)
        }

        if (locationHelper.checkPlayServices(mActivity)) {
            buildGoogleApiClient()
            locationHelper.createLocationRequest()
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationHelper.mLocationRequest, this)

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

        pos = LatLng(latitude, longitude)

        try {
            val success = mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_style))
            if (!success) {
            }
        } catch (e: Resources.NotFoundException) {
        }

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 18f))

        mGoogleMap.setOnCameraIdleListener {
            reactAccordingly(mGoogleMap.cameraPosition.target)
        }
    }

    private fun reactAccordingly(latLng: LatLng) {
        disposable.add(locationHelper.getAddress(latLng, mActivity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<String>() {
            override fun onComplete() {
                // do nothing
            }

            override fun onNext(t: String) {
                mGoogleMap.clear()
                rootView.pbAddress.visibility = View.GONE
                rootView.tvAddress.text = t
                rootView.tvDistance.text = locationHelper.getDistance(mGoogleMap.cameraPosition.target, pos)
                rootView.btSetAlarm.isEnabled = true

                var radius = context!!.resources.getStringArray(R.array.locationPrecision)[appPreferences.locationPrecisionArrayPosition].split(" ")[0]
                if (radius.length < 2)
                    radius = (radius.toDouble() * 1000).toString()
                mGoogleMap.addCircle(CircleOptions()
                        .center(mGoogleMap.cameraPosition.target)
                        .radius(radius.toDouble())
                        .strokeColor(ContextCompat.getColor(mActivity, R.color.colorAccent))
                        .fillColor(ContextCompat.getColor(mActivity, R.color.dayDeselected)))
            }

            override fun onError(e: Throwable) {
                Utils.showSnackBar("It should not take this long, please check your Internet or try again later", rootView)
                return
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
        locationHelper.checkPlayServices(mActivity)

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
        job.cancel()
        disposable.clear()
    }
}
