package com.mdkashif.universalarm.alarm.location

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.ContainerActivity
import com.mdkashif.universalarm.alarm.misc.AlarmOps
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.persistence.RoomRepository
import com.mdkashif.universalarm.utils.AppConstants
import io.reactivex.Observable
import java.text.DecimalFormat
import java.util.*


object LocationHelper {
    lateinit var mLocationRequest: LocationRequest

    private var distance: String = ""
    private var address: String = ""
    private var city: String = ""
    private var destinationLatitude: Double = 0.0
    private var destinationLongitude: Double = 0.0

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
                context.showToast("This device is not supported!!")
                context.onBackPressed()
            }
            return false
        }
        return true
    }

    fun getBitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
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

    fun setAlarm(context: ContainerActivity) {
        RoomRepository.amendLocationsAsync(context.returnDbInstance(), AlarmOps.Add.toString(), LocationsModel(address = address, city = city, latitude = destinationLatitude, longitude = destinationLongitude, status = true))
        context.showToast("All set!, You are $distance, we will notify you, once you reach within the ${context.resources.getStringArray(R.array.locationPrecision)[AppPreferences.locationPrecisionArrayPosition]} destination radius")
        context.onBackPressed()
    }
}