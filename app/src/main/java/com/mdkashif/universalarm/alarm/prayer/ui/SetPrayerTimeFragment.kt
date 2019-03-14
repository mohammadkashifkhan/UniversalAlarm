package com.mdkashif.universalarm.alarm.prayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.CompoundButton
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.misc.AlarmHelper
import com.mdkashif.universalarm.alarm.misc.AlarmOps
import com.mdkashif.universalarm.alarm.misc.AlarmTypes
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import com.mdkashif.universalarm.alarm.prayer.misc.Compass
import com.mdkashif.universalarm.base.BaseFragment
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.persistence.RoomRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_set_prayer_time.*
import kotlinx.android.synthetic.main.fragment_set_prayer_time.view.*


class SetPrayerTimeFragment : BaseFragment(), CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private var timingsList: List<TimingsModel> = ArrayList()

    private var compass: Compass? = null
    private var currentAzimuth: Float = 0.toFloat()
    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_set_prayer_time, container, false)

        setupCompass()
        rootView.swFajr.setOnCheckedChangeListener(this)
        rootView.swSunrise.setOnCheckedChangeListener(this)
        rootView.swDhuhr.setOnCheckedChangeListener(this)
        rootView.swAsr.setOnCheckedChangeListener(this)
        rootView.swSunset.setOnCheckedChangeListener(this)
        rootView.swMaghrib.setOnCheckedChangeListener(this)
        rootView.swIsha.setOnCheckedChangeListener(this)
        rootView.swImsak.setOnCheckedChangeListener(this)
        rootView.swMidnight.setOnCheckedChangeListener(this)
        rootView.clSendFeedback.setOnClickListener(this)

        rootView.tvTimezone.text = AppPreferences.timezone
        rootView.tvIslamicDate.text = AppPreferences.islamicDate
        rootView.tvMonth.text = AppPreferences.islamicMonth
        timingsList = RoomRepository.fetchDataAsync(mActivity.returnDbInstance(), AlarmTypes.Prayer).first!! // Pair's first value

        for (i in timingsList.indices) {
            when (timingsList[i].alarmType) {
                "Fajr" -> {
                    rootView.tvFajr.text = """${timingsList[i].hour}:${timingsList[i].minute}"""
                    rootView.swFajr.isChecked = timingsList[i].status
                }

                "Sunrise" -> {
                    rootView.tvSunrise.text = """${timingsList[i].hour}:${timingsList[i].minute}"""
                    rootView.swSunrise.isChecked = timingsList[i].status
                }

                "Dhuhr" -> {
                    rootView.tvDhuhr.text = """${timingsList[i].hour}:${timingsList[i].minute}"""
                    rootView.swDhuhr.isChecked = timingsList[i].status
                }

                "Asr" -> {
                    rootView.tvAsr.text = """${timingsList[i].hour}:${timingsList[i].minute}"""
                    rootView.swAsr.isChecked = timingsList[i].status
                }

                "Sunset" -> {
                    rootView.tvSunset.text = """${timingsList[i].hour}:${timingsList[i].minute}"""
                    rootView.swSunset.isChecked = timingsList[i].status
                }

                "Maghrib" -> {
                    rootView.tvMaghrib.text = """${timingsList[i].hour}:${timingsList[i].minute}"""
                    rootView.swMaghrib.isChecked = timingsList[i].status
                }

                "Isha" -> {
                    rootView.tvIsha.text = """${timingsList[i].hour}:${timingsList[i].minute}"""
                    rootView.swIsha.isChecked = timingsList[i].status
                }

                "Imsak" -> {
                    rootView.tvImsak.text = """${timingsList[i].hour}:${timingsList[i].minute}"""
                    rootView.swImsak.isChecked = timingsList[i].status
                }

                "Midnight" -> {
                    rootView.tvMidnight.text = """${timingsList[i].hour}:${timingsList[i].minute}"""
                    rootView.swMidnight.isChecked = timingsList[i].status
                }
            }
        }

        return rootView
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when (p0!!.id) {
            R.id.swFajr -> switchPrayerStatus(AlarmTypes.Fajr, p1)
            R.id.swSunrise -> switchPrayerStatus(AlarmTypes.Sunrise, p1)
            R.id.swDhuhr -> switchPrayerStatus(AlarmTypes.Dhuhr, p1)
            R.id.swAsr -> switchPrayerStatus(AlarmTypes.Asr, p1)
            R.id.swSunset -> switchPrayerStatus(AlarmTypes.Sunset, p1)
            R.id.swMaghrib -> switchPrayerStatus(AlarmTypes.Maghrib, p1)
            R.id.swIsha -> switchPrayerStatus(AlarmTypes.Isha, p1)
            R.id.swImsak -> switchPrayerStatus(AlarmTypes.Imsak, p1)
            R.id.swMidnight -> switchPrayerStatus(AlarmTypes.Midnight, p1)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.clSendFeedback -> mActivity.sendFeedback()
        }
    }

    private fun switchPrayerStatus(type: AlarmTypes, status: Boolean) {
        val index = returnPrayerIndex(type)
        timingsList[index].status = status
        RoomRepository.amendTimingsAsync(mActivity.returnDbInstance(), AlarmOps.Add.toString(), timingsList[index])

        if (status)
            AlarmHelper.setAlarm(timingsList[index].hour.toInt(), timingsList[index].minute.toInt(), timingsList[index].pIntentRequestCode.toInt(), mActivity, type)
        else
            AlarmHelper.stopAlarm(timingsList[index].pIntentRequestCode.toInt(), mActivity)
    }

    private fun returnPrayerIndex(type: AlarmTypes): Int {
        for (i in timingsList.indices)
            if (timingsList[i].alarmType == type.toString()) {
                return i
            }
        return 0
    }

    private fun setupCompass() {
        compass = Compass(mActivity)
        val cl = getCompassListener()
        compass!!.setListener(cl)
    }

    private fun adjustArrow(azimuth: Float) {
        val an = RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f)
        currentAzimuth = azimuth

        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true

        ivCompassHands.startAnimation(an)
    }

    private fun getCompassListener(): Compass.CompassListener {
        return object : Compass.CompassListener {
            override fun onNewAzimuth(azimuth: Float) {
                mActivity.runOnUiThread {
                    adjustArrow(azimuth)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        compass!!.start()
    }

    override fun onPause() {
        super.onPause()
        compass!!.stop()
    }

    override fun onStop() {
        super.onStop()
        compass!!.stop()
    }

    override fun onResume() {
        super.onResume()
        compass!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
