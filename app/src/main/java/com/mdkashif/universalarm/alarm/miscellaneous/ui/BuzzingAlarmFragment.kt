package com.mdkashif.universalarm.alarm.miscellaneous.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmSoundService
import com.mdkashif.universalarm.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_buzzing_alarm.view.*

class BuzzingAlarmFragment : BaseFragment() {
    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_buzzing_alarm, container, false)

        rootView.rippleBackground.startRippleAnimation()

        rootView.cancel.setOnClickListener{
            mActivity.stopService(Intent(context, AlarmSoundService::class.java))
            rootView.rippleBackground.stopRippleAnimation()
            mActivity.finish()
        }
        return inflater.inflate(R.layout.fragment_buzzing_alarm, container, false)
    }
}
