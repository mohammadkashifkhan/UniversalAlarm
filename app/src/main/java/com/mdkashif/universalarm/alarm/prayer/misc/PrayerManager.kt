package com.mdkashif.universalarm.alarm.prayer.misc

import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.universalarm.persistence.AppPreferences
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrayerManager : KoinComponent {
    private val apiInterface: ApiInterface = get()

    private val appPreferences: AppPreferences by inject()

    fun getPrayerDetails(onSuccess: (PrayerApiResponse) -> Unit, onFailure: (String) -> Unit) {
         apiInterface.getPrayerDetails(appPreferences.city!!, appPreferences.country!!).
                 enqueue(object : Callback<PrayerApiResponse> {
            override fun onResponse(call: Call<PrayerApiResponse>, response: Response<PrayerApiResponse>) {
                onSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<PrayerApiResponse>, t: Throwable) {
                onFailure("Internal Server Error")
            }
        })
    }
}
