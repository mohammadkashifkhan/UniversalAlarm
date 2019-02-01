package com.mdkashif.universalarm.alarm.prayer.geocoder

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Message
import java.io.IOException
import java.util.*

/**
 * Created by Kashif on 07-Feb-18.
 */

object GetLocationAddress {
    private var city: String? = null
    private var country: String? = null

    fun getAddressFromLocation(latitude: Double, longitude: Double,
                               context: Context, handler: Handler) {
        val thread = object : Thread() {
            override fun run() {
                val mGeocoder = Geocoder(context, Locale.getDefault())
                try {
                    val addressList = mGeocoder.getFromLocation(
                            latitude, longitude, 1)
                    if (addressList != null && addressList.size > 0) {
                        city = addressList[0].locality
                        country = addressList[0].countryName
                    }
                } catch (e: IOException) {
                } finally {
                    val message = Message.obtain()
                    message.target = handler
                    if (!(city != null && city == "")) {
                        message.what = 1
                        val bundle = Bundle()
                        bundle.putString("city", city)
                        bundle.putString("country", country)
                        message.data = bundle
                    } else {
                        message.what = 0
                    }
                    message.sendToTarget()
                }
            }
        }
        thread.start()
    }
}
