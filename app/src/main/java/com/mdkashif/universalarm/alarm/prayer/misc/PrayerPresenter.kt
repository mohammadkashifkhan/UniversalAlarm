package com.mdkashif.universalarm.alarm.prayer.misc

import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import io.reactivex.disposables.CompositeDisposable

class PrayerPresenter(private val disposable: CompositeDisposable, private val prayerViewCallback: PrayerViewCallback, private val city: String, private val country: String) : PrayerManager.PrayerPresenterCallback {
    private var prayerManager = PrayerManager(this)

    fun getPrayerDetails() {
        if(city!="")
            prayerManager.getPrayerDetails(disposable, city, country)
        else
            prayerManager.getPrayerDetails(disposable, "Delhi", "India") // Defaulting to Delhi
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