package com.mdkashif.alarm.battery

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdkashif.alarm.R
import com.mdkashif.alarm.battery.pojo.BatteryStats
import kotlinx.android.synthetic.main.fragment_add_battery_level.*

class SetBatteryLevelFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view : View =inflater.inflate(R.layout.fragment_add_battery_level, container, false)
        val batteryLiveData = BatteryLiveData(activity)
        batteryLiveData.observe(this, Observer<BatteryStats> { connection ->
            batteryChangeMeter.isShowTextWhileSpinning = true
            batteryChangeMeter.setValueAnimated(connection!!.level.toFloat())

            batteryStatus.text = connection.status

            temperature.text = connection.temp.toString()
        })
        return view
    }

}
