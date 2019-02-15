package com.mdkashif.universalarm.alarm.prayer.misc

import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.universalarm.connectivity.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class PrayerManager(val prayerPresenterCallback: PrayerPresenterCallback) {

    fun getPrayerDetails(disposable: CompositeDisposable, city: String, country: String) {

        val apiInterface = ServiceGenerator.client.create(ApiInterface::class.java)
        val prayerCall = apiInterface.getPrayerDetails(city, country)

        disposable.add(prayerCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PrayerApiResponse>() {
            override fun onSuccess(t: PrayerApiResponse) {
                prayerPresenterCallback.onGetPrayerDetails(t)
            }

            override fun onError(e: Throwable) {
                prayerPresenterCallback.onError(e.message!!)
            }
        }))
    }

    interface PrayerPresenterCallback {
        fun onGetPrayerDetails(prayerApiResponse: PrayerApiResponse?)
        fun onError(error: String)
    }
}