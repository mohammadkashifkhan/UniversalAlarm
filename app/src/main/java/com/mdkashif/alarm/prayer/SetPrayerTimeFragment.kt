package com.mdkashif.alarm.prayer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.HomeActivity
import com.mdkashif.alarm.base.BaseActivity.city
import com.mdkashif.alarm.base.BaseActivity.country
import com.mdkashif.alarm.prayer.compass.Compass
import com.mdkashif.alarm.prayer.pojos.PrayerApiResponse
import kotlinx.android.synthetic.main.fragment_set_prayer_time.*

class SetPrayerTimeFragment : Fragment(), PrayerPresenter.PrayerViewCallback {
    private var mActivity: HomeActivity?=null

    private val TAG = SetPrayerTimeFragment::class.java.simpleName

    private var compass: Compass? = null

    private var currentAzimuth: Float = 0.toFloat()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View =inflater.inflate(R.layout.fragment_set_prayer_time, container, false)
        mActivity!!.showLoader()
        if (mActivity!!.isOnline)
            if (!(mActivity!!.isBlank(city) && (mActivity!!.isBlank(country))))
                PrayerPresenter(this,city,country).getPrayerDetails()

        setupCompass()
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mActivity = context as HomeActivity
    }

    private fun setupCompass() {
        compass = Compass(activity)
        val cl = Compass.CompassListener { azimuth -> adjustArrow(azimuth) }
        compass!!.setListener(cl)
    }

    private fun adjustArrow(azimuth: Float) {
        Log.d(TAG, "will set rotation from " + currentAzimuth + " to "
                + azimuth)

        val an = RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f)
        currentAzimuth = azimuth

        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true

        compass_hands.startAnimation(an)
    }

    override fun onPrayerDetailSuccess(prayerApiResponse: PrayerApiResponse?) {
        mActivity!!.hideLoader()
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
       mActivity!!.showSnackbar(error, activity)
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
