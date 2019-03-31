package com.mdkashif.universalarm.alarm.prayer.job

import android.app.job.JobParameters
import android.app.job.JobService
import com.mdkashif.universalarm.alarm.misc.AlarmHelper
import com.mdkashif.universalarm.alarm.misc.AlarmTypes
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import com.mdkashif.universalarm.alarm.prayer.misc.PrayerPresenter
import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.persistence.RoomRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PrayerDataFetchScheduleService constructor(@Inject val roomRepository: RoomRepository) : JobService(), PrayerPresenter.PrayerViewCallback {
    private val disposable = CompositeDisposable()
    private lateinit var params: JobParameters

    override fun onStartJob(params: JobParameters): Boolean {
        this.params = params
        PrayerPresenter(disposable, this, applicationContext).getPrayerDetails()
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        disposable.clear()
        return false
    }

    override fun onPrayerDetailSuccess(prayerApiResponse: PrayerApiResponse?) {
        jobFinished(params, false)
        AppPreferences().instance.timezone = prayerApiResponse!!.data!!.meta!!.timezone!!
        AppPreferences().instance.islamicDate = prayerApiResponse.data!!.date!!.hijri!!.date!!
        AppPreferences().instance.islamicMonth = prayerApiResponse.data.date!!.hijri!!.month!!.en!!

        val sunsetTiming = prayerApiResponse.data.timings!!.sunset!!.split(":")
        val sunsetTimingsModel = TimingsModel(hour = sunsetTiming[0], minute = sunsetTiming[1], alarmType = AlarmTypes.Sunset.toString(), status = false, pIntentRequestCode = AlarmHelper.returnPendingIntentUniqueRequestCode().toLong())
        roomRepository.amendPrayerAlarmsAsync(sunsetTimingsModel, autoUpdate = true)

        val asrTiming = prayerApiResponse.data.timings.asr!!.split(":")
        val asrTimingsModel = TimingsModel(hour = asrTiming[0], minute = asrTiming[1], alarmType = AlarmTypes.Asr.toString(), status = false, pIntentRequestCode = AlarmHelper.returnPendingIntentUniqueRequestCode().toLong())
        roomRepository.amendPrayerAlarmsAsync(asrTimingsModel, autoUpdate = true)

        val ishaTiming = prayerApiResponse.data.timings.isha!!.split(":")
        val ishaTimingsModel = TimingsModel(hour = ishaTiming[0], minute = ishaTiming[1], alarmType = AlarmTypes.Isha.toString(), status = false, pIntentRequestCode = AlarmHelper.returnPendingIntentUniqueRequestCode().toLong())
        roomRepository.amendPrayerAlarmsAsync(ishaTimingsModel, autoUpdate = true)

        val fajrTiming = prayerApiResponse.data.timings.fajr!!.split(":")
        val fajrTimingsModel = TimingsModel(hour = fajrTiming[0], minute = fajrTiming[1], alarmType = AlarmTypes.Fajr.toString(), status = false, pIntentRequestCode = AlarmHelper.returnPendingIntentUniqueRequestCode().toLong())
        roomRepository.amendPrayerAlarmsAsync(fajrTimingsModel, autoUpdate = true)

        val dhuhrTiming = prayerApiResponse.data.timings.dhuhr!!.split(":")
        val dhuhrTimingsModel = TimingsModel(hour = dhuhrTiming[0], minute = dhuhrTiming[1], alarmType = AlarmTypes.Dhuhr.toString(), status = false, pIntentRequestCode = AlarmHelper.returnPendingIntentUniqueRequestCode().toLong())
        roomRepository.amendPrayerAlarmsAsync(dhuhrTimingsModel, autoUpdate = true)

        val maghribTiming = prayerApiResponse.data.timings.maghrib!!.split(":")
        val maghribTimingsModel = TimingsModel(hour = maghribTiming[0], minute = maghribTiming[1], alarmType = AlarmTypes.Maghrib.toString(), status = false, pIntentRequestCode = AlarmHelper.returnPendingIntentUniqueRequestCode().toLong())
        roomRepository.amendPrayerAlarmsAsync(maghribTimingsModel, autoUpdate = true)

        val sunriseTiming = prayerApiResponse.data.timings.sunrise!!.split(":")
        val sunriseTimingsModel = TimingsModel(hour = sunriseTiming[0], minute = sunriseTiming[1], alarmType = AlarmTypes.Sunrise.toString(), status = false, pIntentRequestCode = AlarmHelper.returnPendingIntentUniqueRequestCode().toLong())
        roomRepository.amendPrayerAlarmsAsync(sunriseTimingsModel, autoUpdate = true)

        val midnightTiming = prayerApiResponse.data.timings.midnight!!.split(":")
        val midnightTimingsModel = TimingsModel(hour = midnightTiming[0], minute = midnightTiming[1], alarmType = AlarmTypes.Midnight.toString(), status = false, pIntentRequestCode = AlarmHelper.returnPendingIntentUniqueRequestCode().toLong())
        roomRepository.amendPrayerAlarmsAsync(midnightTimingsModel, autoUpdate = true)

        val imsakTiming = prayerApiResponse.data.timings.imsak!!.split(":")
        val imsakTimingsModel = TimingsModel(hour = imsakTiming[0], minute = imsakTiming[1], alarmType = AlarmTypes.Imsak.toString(), status = false, pIntentRequestCode = AlarmHelper.returnPendingIntentUniqueRequestCode().toLong())
        roomRepository.amendPrayerAlarmsAsync(imsakTimingsModel, autoUpdate = true)

        PrayerDataFetchScheduler.scheduleJob(applicationContext)
    }

    override fun onError(error: String) {
        jobFinished(params, false)
        disposable.clear()
        PrayerDataFetchScheduler.scheduleJob(applicationContext)
    }
}
