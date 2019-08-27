package com.mdkashif.universalarm.alarm.prayer.misc

import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("timingsByCity?method=1")
    fun getPrayerDetails(@Query("city") city: String?, @Query("country") country: String?): Call<PrayerApiResponse>
}