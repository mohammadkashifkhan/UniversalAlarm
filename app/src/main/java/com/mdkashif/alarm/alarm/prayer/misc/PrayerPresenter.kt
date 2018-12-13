package com.mdkashif.alarm.alarm.prayer.misc

import com.mdkashif.alarm.alarm.prayer.model.PrayerApiResponse

class PrayerPresenter(private val prayerViewCallback: PrayerViewCallback, city: String, country: String) : PrayerManager.PrayerPresenterCallback {
    private var prayerManager = PrayerManager(this)
    private var city : String = ""
    private var country : String = ""

    init {
        this.city = city
        this.country = country
    }

    fun getPrayerDetails() {
        prayerManager.getPrayerDetails(city,country)
    }

    override fun onGetPrayerDetails(prayerApiResponse: PrayerApiResponse?) {
        prayerViewCallback.onPrayerDetailSuccess(prayerApiResponse)
    }

    override fun onError(error: String) {
        prayerViewCallback.onError(error)
    }

    interface PrayerViewCallback {
        fun onPrayerDetailSuccess(prayerApiResponse: PrayerApiResponse?)
        fun onError(error: String)
    }

}