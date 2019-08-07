package com.mdkashif.universalarm.alarm.prayer.misc

import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.universalarm.persistence.AppPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class PrayerManager : KoinComponent {
    private val apiInterface: ApiInterface by inject()

    private val appPreferences: AppPreferences by inject()

    fun getPrayerDetails(disposable: CompositeDisposable, onSuccess: (PrayerApiResponse) -> Unit, onFailure: (String) -> Unit) {
        val prayerCall = apiInterface.getPrayerDetails(appPreferences.city!!, appPreferences.country!!)

        disposable.add(prayerCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PrayerApiResponse>() {
                    override fun onSuccess(t: PrayerApiResponse) {
                        onSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        onFailure(e.message!!)
                    }
                }))
    }
}
