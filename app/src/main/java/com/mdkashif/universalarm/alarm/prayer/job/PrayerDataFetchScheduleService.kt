package com.mdkashif.universalarm.alarm.prayer.job

import android.app.job.JobParameters
import android.app.job.JobService

import com.mdkashif.universalarm.alarm.prayer.misc.PrayerPresenter
import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder
import io.reactivex.disposables.CompositeDisposable

class PrayerDataFetchScheduleService : JobService(), PrayerPresenter.PrayerViewCallback {
    private val disposable = CompositeDisposable()

    override fun onStartJob(params: JobParameters): Boolean {
        PrayerPresenter(disposable, this, SharedPrefHolder.getInstance(applicationContext).city,
                        SharedPrefHolder.getInstance(applicationContext).country).getPrayerDetails()
        jobFinished(params, false)
        PrayerDataFetchScheduler.scheduleJob(applicationContext)
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        disposable.clear()
        return false
    }

    override fun onPrayerDetailSuccess(prayerApiResponse: PrayerApiResponse?) {
        // TODO: insert in db
    }

    override fun onError(error: String) {
        disposable.clear()
        // do nothing
    }
}
