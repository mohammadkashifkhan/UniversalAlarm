package com.mdkashif.alarm.alarm.battery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.ContainerActivity
import com.mdkashif.alarm.alarm.battery.pojo.BatteryStats
import com.mdkashif.alarm.utils.SharedPrefHolder
import kotlinx.android.synthetic.main.fragment_add_battery_level.*
import kotlinx.android.synthetic.main.fragment_add_battery_level.view.*


class SetBatteryLevelFragment : Fragment() {
    private lateinit var mActivity: ContainerActivity

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

        view.swBattery.isChecked = SharedPrefHolder.getInstance(activity).batteryAlarmStatus == true
        view.theftSwitch.isChecked = SharedPrefHolder.getInstance(activity).theftAlarmStatus == true

        if((SharedPrefHolder.getInstance(activity).hbl == 0f) && (SharedPrefHolder.getInstance(activity).lbl == 0f))
            view.rbBatteryLevel.setRangePinsByValue(20f,85f)
        else if (SharedPrefHolder.getInstance(activity).hbl == 0f)
            view.rbBatteryLevel.setRangePinsByValue(SharedPrefHolder.getInstance(activity).lbl,85f)
        else if (SharedPrefHolder.getInstance(activity).lbl == 0f)
            view.rbBatteryLevel.setRangePinsByValue(20f,SharedPrefHolder.getInstance(activity).hbl)
        else
            view.rbBatteryLevel.setRangePinsByValue(SharedPrefHolder.getInstance(activity).lbl,SharedPrefHolder.getInstance(activity).hbl)


        if(SharedPrefHolder.getInstance(activity).temp == 0f)
            view.rbTemp.setSeekPinByValue(35f)
        else
            view.rbTemp.setSeekPinByValue(SharedPrefHolder.getInstance(activity).temp)

        view.rbBatteryLevel.setOnRangeBarChangeListener { rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue ->
            if(SharedPrefHolder.getInstance(activity).hbl != rightPinValue.toFloat())
                SharedPrefHolder.getInstance(activity).hbl = rightPinValue.toFloat()
            if(SharedPrefHolder.getInstance(activity).lbl != leftPinValue.toFloat())
                SharedPrefHolder.getInstance(activity).lbl = leftPinValue.toFloat()
        }

        view.rbTemp.setOnRangeBarChangeListener { rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue ->
            if(SharedPrefHolder.getInstance(activity).temp != rightPinValue.toFloat())
                SharedPrefHolder.getInstance(activity).temp = rightPinValue.toFloat()
        }

        view.swBattery.setOnCheckedChangeListener{ buttonView, isChecked ->
            SharedPrefHolder.getInstance(activity).batteryAlarmStatus = isChecked
        }

        view.theftSwitch.setOnCheckedChangeListener{ buttonView, isChecked ->
            SharedPrefHolder.getInstance(activity).theftAlarmStatus = isChecked
        }
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mActivity = context as ContainerActivity
    }

}
