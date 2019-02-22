package com.mdkashif.universalarm.alarm.prayer.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.ContainerActivity
import com.mdkashif.universalarm.alarm.prayer.misc.Compass
import com.mdkashif.universalarm.alarm.prayer.misc.PrayerPresenter
import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_set_prayer_time.*


class SetPrayerTimeFragment : Fragment(), PrayerPresenter.PrayerViewCallback, CompoundButton.OnCheckedChangeListener {

    private lateinit var mActivity: ContainerActivity

    private var compass: Compass? = null

    private var currentAzimuth: Float = 0.toFloat()
    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView: View =inflater.inflate(R.layout.fragment_set_prayer_time, container, false)

        mActivity.showLoader()
        if (mActivity.isOnline)
            PrayerPresenter(disposable,this, SharedPrefHolder.getInstance(context).city!!, SharedPrefHolder.getInstance(context).country!!).getPrayerDetails()

        setupCompass()
        return rootView
    }

    override fun onAttach(context: Context) {
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
        tvTimezone.text = prayerApiResponse!!.data!!.meta!!.timezone
        tvIslamicDate.text = prayerApiResponse.data!!.date!!.hijri!!.date
        tvMonth.text = prayerApiResponse.data.date!!.hijri!!.month!!.en
        tvFajr.text = prayerApiResponse.data.timings!!.fajr
        tvSunrise.text = prayerApiResponse.data.timings.sunrise
        tvDhuhr.text = prayerApiResponse.data.timings.dhuhr
        tvAsr.text = prayerApiResponse.data.timings.asr
        tvSunset.text = prayerApiResponse.data.timings.sunset
        tvMaghrib.text = prayerApiResponse.data.timings.maghrib
        tvIsha.text = prayerApiResponse.data.timings.isha
        tvImsak.text = prayerApiResponse.data.timings.imsak
        tvMidnight.text = prayerApiResponse.data.timings.midnight
    }

    override fun onError(error: String) {
        mActivity.hideLoader()
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

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
