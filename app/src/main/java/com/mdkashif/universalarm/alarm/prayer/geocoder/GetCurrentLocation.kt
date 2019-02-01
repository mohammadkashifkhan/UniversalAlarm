package com.mdkashif.universalarm.alarm.prayer.geocoder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

import androidx.core.app.ActivityCompat


class GetCurrentLocation private constructor() {
    private var locationManager: LocationManager? = null

    fun findLocation(con: Context): Location? {
        locationManager = con.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager!!.getProviders(true)
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            locationManager!!.requestLocationUpdates(provider, 1000, 0f,
                    object : LocationListener {

                        override fun onLocationChanged(location: Location) {}

                        override fun onProviderDisabled(provider: String) {}

                        override fun onProviderEnabled(provider: String) {}

                        override fun onStatusChanged(provider: String, status: Int,
                                                     extras: Bundle) {
                        }
                    })
            location = locationManager!!.getLastKnownLocation(provider)
        }
        return location
    }

    companion object {
        val instance = GetCurrentLocation()

        private var location: Location? = null
    }
}
