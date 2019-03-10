package com.mdkashif.universalarm.alarm.misc.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.misc.AlarmHelper
import com.mdkashif.universalarm.alarm.misc.AlarmSoundService
import com.mdkashif.universalarm.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_buzzing_alarm.view.*

class BuzzingAlarmFragment : BaseFragment() {
    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_buzzing_alarm, container, false)

        rootView.rippleBackground.startRippleAnimation()

        val requestCode = this.arguments!!["param2"].toString()
        rootView.tvNote.text = this.arguments!!["note"].toString()
        rootView.tvDateTime.text = "${this.arguments!!["hour"]}:${this.arguments!!["minute"]}"

        rootView.fbCancel.setOnClickListener {
            mActivity.stopService(Intent(context, AlarmSoundService::class.java))
            rootView.rippleBackground.stopRippleAnimation()
            AlarmHelper.stopAlarm(requestCode.toInt(), mActivity)
            mActivity.finish()
        }
        return inflater.inflate(R.layout.fragment_buzzing_alarm, container, false)
    }
}
