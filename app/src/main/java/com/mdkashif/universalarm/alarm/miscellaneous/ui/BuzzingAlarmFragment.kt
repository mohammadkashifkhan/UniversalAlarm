package com.mdkashif.universalarm.alarm.miscellaneous.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.ContainerActivity
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmSoundService
import kotlinx.android.synthetic.main.fragment_buzzing_alarm.view.*

class BuzzingAlarmFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var mActivity: ContainerActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_buzzing_alarm, container, false)

        rootView.rippleBackground.startRippleAnimation()

        rootView.cancel.setOnClickListener{
            mActivity.stopService(Intent(context, AlarmSoundService::class.java))
            rootView.rippleBackground.stopRippleAnimation()
            activity!!.finish()
        }
        return inflater.inflate(R.layout.fragment_buzzing_alarm, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as ContainerActivity
    }

}
