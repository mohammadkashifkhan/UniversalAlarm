package com.mdkashif.universalarm.alarm.prayer.job

import android.app.job.JobParameters
import android.app.job.JobService
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmOps
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmTypes
import com.mdkashif.universalarm.alarm.miscellaneous.model.TimingsModel
import com.mdkashif.universalarm.alarm.prayer.misc.PrayerPresenter
import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.universalarm.utils.persistence.AppDatabase
import com.mdkashif.universalarm.utils.persistence.RoomHelper
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder
import io.reactivex.disposables.CompositeDisposable

class PrayerDataFetchScheduleService : JobService(), PrayerPresenter.PrayerViewCallback {
    private val disposable = CompositeDisposable()
    private lateinit var params: JobParameters

    override fun onStartJob(params: JobParameters): Boolean {
        this.params = params
        PrayerPresenter(disposable, this, SharedPrefHolder.getInstance(applicationContext).city!!,
                SharedPrefHolder.getInstance(applicationContext).country!!).getPrayerDetails()
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        disposable.clear()
        return false
    }

    override fun onPrayerDetailSuccess(prayerApiResponse: PrayerApiResponse?) {
        jobFinished(params, false)

        SharedPrefHolder.getInstance(applicationContext).timezone = prayerApiResponse!!.data!!.meta!!.timezone!!
        SharedPrefHolder.getInstance(applicationContext).islamicDate = prayerApiResponse.data!!.date!!.hijri!!.date!!
        SharedPrefHolder.getInstance(applicationContext).islamicMonth = prayerApiResponse.data.date!!.hijri!!.month!!.en!!

        val sunsetTiming = prayerApiResponse.data.timings!!.sunset!!.split(":")
        val sunsetTimingsModel = TimingsModel(hour = sunsetTiming[0], minute = sunsetTiming[1], alarmType = AlarmTypes.Sunset.toString(), status = true)
        RoomHelper.transactAmendAsync(AppDatabase.getAppDatabase(applicationContext), AlarmOps.Add.toString(), sunsetTimingsModel)

        val asrTiming = prayerApiResponse.data.timings.asr!!.split(":")
        val asrTimingsModel = TimingsModel(hour = asrTiming[0], minute = asrTiming[1], alarmType = AlarmTypes.Asr.toString(), status = true)
        RoomHelper.transactAmendAsync(AppDatabase.getAppDatabase(applicationContext), AlarmOps.Add.toString(), asrTimingsModel)

        val ishaTiming = prayerApiResponse.data.timings.isha!!.split(":")
        val ishaTimingsModel = TimingsModel(hour = ishaTiming[0], minute = ishaTiming[1], alarmType = AlarmTypes.Isha.toString(), status = true)
        RoomHelper.transactAmendAsync(AppDatabase.getAppDatabase(applicationContext), AlarmOps.Add.toString(), ishaTimingsModel)

        val fajrTiming = prayerApiResponse.data.timings.fajr!!.split(":")
        val fajrTimingsModel = TimingsModel(hour = fajrTiming[0], minute = fajrTiming[1], alarmType = AlarmTypes.Fajr.toString(), status = true)
        RoomHelper.transactAmendAsync(AppDatabase.getAppDatabase(applicationContext), AlarmOps.Add.toString(), fajrTimingsModel)

        val dhuhrTiming = prayerApiResponse.data.timings.dhuhr!!.split(":")
        val dhuhrTimingsModel = TimingsModel(hour = dhuhrTiming[0], minute = dhuhrTiming[1], alarmType = AlarmTypes.Dhuhr.toString(), status = true)
        RoomHelper.transactAmendAsync(AppDatabase.getAppDatabase(applicationContext), AlarmOps.Add.toString(), dhuhrTimingsModel)

        val maghribTiming = prayerApiResponse.data.timings.maghrib!!.split(":")
        val maghribTimingsModel = TimingsModel(hour = maghribTiming[0], minute = maghribTiming[1], alarmType = AlarmTypes.Maghrib.toString(), status = true)
        RoomHelper.transactAmendAsync(AppDatabase.getAppDatabase(applicationContext), AlarmOps.Add.toString(), maghribTimingsModel)

        val sunriseTiming = prayerApiResponse.data.timings.sunrise!!.split(":")
        val sunriseTimingsModel = TimingsModel(hour = sunriseTiming[0], minute = sunriseTiming[1], alarmType = AlarmTypes.Sunrise.toString(), status = true)
        RoomHelper.transactAmendAsync(AppDatabase.getAppDatabase(applicationContext), AlarmOps.Add.toString(), sunriseTimingsModel)

        val midnightTiming = prayerApiResponse.data.timings.midnight!!.split(":")
        val midnightTimingsModel = TimingsModel(hour = midnightTiming[0], minute = midnightTiming[1], alarmType = AlarmTypes.Midnight.toString(), status = true)
        RoomHelper.transactAmendAsync(AppDatabase.getAppDatabase(applicationContext), AlarmOps.Add.toString(), midnightTimingsModel)

        val imsakTiming = prayerApiResponse.data.timings.imsak!!.split(":")
        val imsakTimingsModel = TimingsModel(hour = imsakTiming[0], minute = imsakTiming[1], alarmType = AlarmTypes.Imsak.toString(), status = true)
        RoomHelper.transactAmendAsync(AppDatabase.getAppDatabase(applicationContext), AlarmOps.Add.toString(), imsakTimingsModel)

        PrayerDataFetchScheduler.scheduleJob(applicationContext)
    }

    override fun onError(error: String) {
        jobFinished(params, false)
        disposable.clear()
        PrayerDataFetchScheduler.scheduleJob(applicationContext)
    }
}
