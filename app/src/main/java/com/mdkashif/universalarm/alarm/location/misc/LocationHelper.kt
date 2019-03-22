package com.mdkashif.universalarm.alarm.location.misc

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.ContainerActivity
import com.mdkashif.universalarm.alarm.misc.AlarmHelper
import com.mdkashif.universalarm.alarm.misc.AlarmOps
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.services.GeofenceTransitionsIntentService
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.persistence.RoomRepository
import com.mdkashif.universalarm.utils.AppConstants
import com.mdkashif.universalarm.utils.Utils
import io.reactivex.Observable
import java.text.DecimalFormat
import java.util.*


object LocationHelper {
    lateinit var mLocationRequest: LocationRequest

    private var distance: String = ""
    private var address: String = ""
    private var city: String = ""
    private var note: String = ""
    private var destinationLatitude: Double = 0.0
    private var destinationLongitude: Double = 0.0


    private lateinit var geofencingClient: GeofencingClient

    fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = AppConstants.updateInterval.toLong()
        mLocationRequest.fastestInterval = AppConstants.fastestInterval.toLong()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.smallestDisplacement = AppConstants.displacement.toFloat()
    }

    fun checkPlayServices(context: ContainerActivity): Boolean {
        val resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context,
                        AppConstants.playServicesResolutionRequest).show()
            } else {
                Utils.showToast("This device is not supported!!", context)
                context.onBackPressed()
            }
            return false
        }
        return true
    }

    fun getAddress(latLng: LatLng, context: ContainerActivity): Observable<String> {
        val mGeocoder = Geocoder(context, Locale.getDefault())

        try {
            val addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null) {
                address = capitalizeFirstLettersOnly(addresses[0].getAddressLine(0).substring(0, addresses[0].getAddressLine(0).indexOf(addresses[0].locality) - 2))
                city = addresses[0].locality
                destinationLatitude = latLng.latitude
                destinationLongitude = latLng.longitude
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Observable.just("$address $city")
    }

    private fun capitalizeFirstLettersOnly(text: String): String {
        val stringArray = text.trim { it <= ' ' }.toCharArray()
        var wordStarted = false
        for (i in stringArray.indices) {
            val ch = stringArray[i]
            if (ch in 'a'..'z' || ch in 'A'..'Z' || ch == '\'') {
                if (!wordStarted) {
                    stringArray[i] = Character.toUpperCase(stringArray[i])
                    wordStarted = true
                }
            } else {
                wordStarted = false
            }
        }
        return String(stringArray)
    }

    fun getDistance(destinationLatLng: LatLng, currentLatLng: LatLng): String {
        val results = SphericalUtil.computeDistanceBetween(destinationLatLng, currentLatLng)
        distance = "${DecimalFormat("#0.00").format((results / 1000))}kms away"
        return distance
    }

    fun setAlarm(context: ContainerActivity, note: String,
                 success: () -> Unit,
                 failure: (error: String) -> Unit) {
        this.note = note
        val pIntentRequestCode = AlarmHelper.returnPendingIntentUniqueRequestCode()
        val dao = LocationsModel(address = address, city = city, latitude = destinationLatitude, longitude = destinationLongitude, note = note, pIntentRequestCode = pIntentRequestCode.toLong(), status = true)
        RoomRepository.amendLocationsAsync(context.returnDbInstance(), AlarmOps.Add.toString(), dao)
        Utils.showToast("All set!, You are $distance, we will notify you, once you reach within the ${context.resources.getStringArray(R.array.locationPrecision)[AppPreferences.locationPrecisionArrayPosition]} destination radius", context)

        reactAccordingly(context, pIntentRequestCode.toLong(), dao, success = { success() }, failure = {})
    }

    fun updateAlarm(context: ContainerActivity, note: String,
                    success: () -> Unit,
                    failure: (error: String) -> Unit, pIntentRequestCode: Long = 0, alarmId: Int = 0) {
        this.note = note

        val dao = LocationsModel(address = address, city = city, latitude = destinationLatitude, longitude = destinationLongitude, note = note, pIntentRequestCode = pIntentRequestCode, status = true)
        RoomRepository.amendLocationsAsync(context.returnDbInstance(), AlarmOps.Update.toString(), dao, alarmId.toLong())
        Utils.showToast("All set!, You are $distance, we will notify you, once you reach within the ${context.resources.getStringArray(R.array.locationPrecision)[AppPreferences.locationPrecisionArrayPosition]} destination radius", context)
        removeAlarm(pIntentRequestCode.toString(), success = {
            reactAccordingly(context, pIntentRequestCode, dao, success = { success() }, failure = {})
        }, failure = {}, context = context)

    }

    private fun reactAccordingly(context: ContainerActivity, pIntentRequestCode: Long, dao: LocationsModel,
                                 success: () -> Unit,
                                 failure: (error: String) -> Unit) {
        val geofence = buildGeofence(destinationLatitude, destinationLongitude, context, pIntentRequestCode.toString())
        if (geofence != null
                && ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geofencingClient = LocationServices.getGeofencingClient(context)

            val geofencePendingIntent: PendingIntent by lazy {
                val intent = Intent(context, GeofenceTransitionsIntentService::class.java)
                intent.putExtra("locationDao", dao)
                PendingIntent.getService(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT)
            }
            geofencingClient
                    .addGeofences(buildGeofencingRequest(geofence), geofencePendingIntent)
                    .addOnSuccessListener {
                        success()
                    }
                    .addOnFailureListener {
                        failure(GeofenceErrorMessages.getErrorString(context, it))
                    }
        }
    }

    private fun buildGeofence(latitude: Double, longitude: Double, context: Context, pIntentRequestCode: String): Geofence? {
        val radius = context.resources.getStringArray(R.array.locationPrecision)[AppPreferences.locationPrecisionArrayPosition].split(" ")[0]

        return Geofence.Builder()
                .setRequestId(pIntentRequestCode)
                .setCircularRegion(
                        latitude,
                        longitude,
                        radius.toFloat()
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build()
    }

    private fun buildGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
                .setInitialTrigger(0)
                .addGeofences(listOf(geofence))
                .build()
    }

    fun removeAlarm(id: String,
                    success: () -> Unit,
                    failure: (error: String) -> Unit, context: Context) {
        geofencingClient
                .removeGeofences(listOf(id))
                .addOnSuccessListener {
                    success()
                }
                .addOnFailureListener {
                    failure(GeofenceErrorMessages.getErrorString(context, it))
                }
    }
}