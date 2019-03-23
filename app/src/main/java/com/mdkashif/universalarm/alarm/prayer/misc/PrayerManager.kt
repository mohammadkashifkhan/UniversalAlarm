package com.mdkashif.universalarm.alarm.prayer.misc

import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.utils.RestClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class PrayerManager(val prayerPresenterCallback: PrayerPresenterCallback) {
    private val apiInterface = RestClient.client.create(ApiInterface::class.java)

    fun getPrayerDetails(disposable: CompositeDisposable) {
        val prayerCall = apiInterface.getPrayerDetails(AppPreferences.city!!, AppPreferences.country!!)

        disposable.add(prayerCall.subscribeOn(Schedulers.io()) // io thread used for fetching data
                .observeOn(AndroidSchedulers.mainThread()) // data thrown to main thread
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
