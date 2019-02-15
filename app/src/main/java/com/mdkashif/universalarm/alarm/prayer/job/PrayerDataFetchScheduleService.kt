package com.mdkashif.universalarm.alarm.prayer.job

import android.app.job.JobParameters
import android.app.job.JobService
import com.mdkashif.universalarm.alarm.prayer.misc.PrayerPresenter
import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder
import io.reactivex.disposables.CompositeDisposable

class PrayerDataFetchScheduleService : JobService(), PrayerPresenter.PrayerViewCallback {
    private val disposable = CompositeDisposable()
    private lateinit var params: JobParameters

    override fun onStartJob(params: JobParameters): Boolean {
        this.params = params
        PrayerPresenter(disposable, this, SharedPrefHolder.getInstance(applicationContext).city,
                SharedPrefHolder.getInstance(applicationContext).country).getPrayerDetails()
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        disposable.clear()
        return false
    }

    override fun onPrayerDetailSuccess(prayerApiResponse: PrayerApiResponse?) {
        jobFinished(params, false)
//        val timingsModel= TimingsModel()
//        RoomHelper.transactAmendAsync(mActivity.returnDbInstance(), AlarmOps.Update.toString(), timingsModel)
        PrayerDataFetchScheduler.scheduleJob(applicationContext)
    }

    override fun onError(error: String) {
        jobFinished(params, false)
        disposable.clear()
        PrayerDataFetchScheduler.scheduleJob(applicationContext)
    }
}
