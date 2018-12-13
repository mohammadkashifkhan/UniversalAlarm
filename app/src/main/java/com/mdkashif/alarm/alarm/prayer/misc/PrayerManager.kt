package com.mdkashif.alarm.alarm.prayer.misc

import com.mdkashif.alarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.alarm.connectivity.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrayerManager(val prayerPresenterCallback: PrayerPresenterCallback) {

    fun getPrayerDetails(city: String, country: String) {

        val apiInterface = ServiceGenerator.getClient().create(ApiInterface::class.java)
        val prayerCall = apiInterface.getPrayerDetails(city,country)

        prayerCall.enqueue(object : Callback<PrayerApiResponse> {
            override fun onResponse(call: Call<PrayerApiResponse>, response: Response<PrayerApiResponse>) {
                prayerPresenterCallback.onGetPrayerDetails(response.body())
//                Log.d("check", response.body().toString())
            }

            override fun onFailure(call: Call<PrayerApiResponse>, t: Throwable) {
                prayerPresenterCallback.onError("Internal Server Error")
            }
        })
    }

    interface PrayerPresenterCallback {
        fun onGetPrayerDetails(prayerApiResponse: PrayerApiResponse?)
        fun onError(error: String)
    }
}
