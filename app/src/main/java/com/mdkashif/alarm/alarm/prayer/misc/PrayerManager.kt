package com.mdkashif.alarm.alarm.prayer.misc

import com.mdkashif.alarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.alarm.connectivity.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class PrayerManager(val prayerPresenterCallback: PrayerPresenterCallback) {

    fun getPrayerDetails(disposable: CompositeDisposable, city: String, country: String) {

        val apiInterface = ServiceGenerator.getClient().create(ApiInterface::class.java)
        val prayerCall = apiInterface.getPrayerDetails(city, country)

//        prayerCall.enqueue(object : Callback<PrayerApiResponse> {
//            override fun onResponse(call: Call<PrayerApiResponse>, response: Response<PrayerApiResponse>) {
//                prayerPresenterCallback.onGetPrayerDetails(response.body())
//            }
//
//            override fun onFailure(call: Call<PrayerApiResponse>, t: Throwable) {
//                prayerPresenterCallback.onError("Internal Server Error")
//            }
//        })

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
