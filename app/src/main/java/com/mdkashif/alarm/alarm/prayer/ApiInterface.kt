package com.mdkashif.alarm.alarm.prayer

import com.mdkashif.alarm.alarm.prayer.pojos.PrayerApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("timingsByCity?method=1")
    fun getPrayerDetails(@Query("city") city: String?, @Query("country") country: String?): Call<PrayerApiResponse>
}