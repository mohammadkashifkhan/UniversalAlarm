package com.mdkashif.alarm.alarm.prayer.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.ContainerActivity
import com.mdkashif.alarm.alarm.prayer.misc.Compass
import com.mdkashif.alarm.alarm.prayer.misc.PrayerPresenter
import com.mdkashif.alarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.alarm.utils.persistence.SharedPrefHolder
import kotlinx.android.synthetic.main.fragment_set_prayer_time.*


class SetPrayerTimeFragment : Fragment(), PrayerPresenter.PrayerViewCallback, CompoundButton.OnCheckedChangeListener {

    private lateinit var mActivity: ContainerActivity

    private var compass: Compass? = null

    private var currentAzimuth: Float = 0.toFloat()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var rootView: View =inflater.inflate(R.layout.fragment_set_prayer_time, container, false)

        mActivity.showLoader()
        if (mActivity.isOnline)
            if (!(mActivity.isBlank(SharedPrefHolder.getInstance(context).city)
                            && (mActivity.isBlank(SharedPrefHolder.getInstance(context).country))))
                PrayerPresenter(this, SharedPrefHolder.getInstance(context).city!!, SharedPrefHolder.getInstance(context).country!!).getPrayerDetails()

        setupCompass()
        return rootView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mActivity = context as ContainerActivity
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when(p0!!.id){

        }
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

        compass_hands.startAnimation(an)
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

    override fun onPrayerDetailSuccess(prayerApiResponse: PrayerApiResponse?) {
        mActivity.hideLoader()
        timezone.text = prayerApiResponse!!.data!!.meta!!.timezone
        date.text = prayerApiResponse.data!!.date!!.hijri!!.date
        month.text = prayerApiResponse.data.date!!.hijri!!.month!!.en
        fajr.text = prayerApiResponse.data.timings!!.fajr
        sunrise.text = prayerApiResponse.data.timings.sunrise
        dhuhr.text = prayerApiResponse.data.timings.dhuhr
        asr.text = prayerApiResponse.data.timings.asr
        sunset.text = prayerApiResponse.data.timings.sunset
        maghrib.text = prayerApiResponse.data.timings.maghrib
        isha.text = prayerApiResponse.data.timings.isha
        imsak.text = prayerApiResponse.data.timings.imsak
        midnight.text = prayerApiResponse.data.timings.midnight
    }

    override fun onError(error: String) {
       mActivity.showSnackBar(error)
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
}
