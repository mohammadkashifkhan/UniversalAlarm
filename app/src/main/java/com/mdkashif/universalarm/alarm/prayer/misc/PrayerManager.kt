package com.mdkashif.universalarm.alarm.prayer.misc

import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.universalarm.persistence.AppPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PrayerManager(val prayerPresenterCallback: PrayerPresenterCallback) {
    @Inject
    lateinit var apiInterface: ApiInterface

    @Inject
    lateinit var appPreferences: AppPreferences

    fun getPrayerDetails(disposable: CompositeDisposable) {
        val prayerCall = apiInterface.getPrayerDetails(appPreferences.city!!, appPreferences.country!!)

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
