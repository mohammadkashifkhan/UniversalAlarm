package com.mdkashif.universalarm.alarm.battery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import com.appyvet.materialrangebar.RangeBar
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.battery.misc.BatteryLiveData
import com.mdkashif.universalarm.alarm.battery.misc.BatteryStatsPoJo
import com.mdkashif.universalarm.base.BaseFragment
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.utils.Utils
import kotlinx.android.synthetic.main.fragment_set_battery_level.view.*


class SetBatteryLevelFragment : BaseFragment(), CompoundButton.OnCheckedChangeListener, RangeBar.OnRangeBarChangeListener, View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_set_battery_level, container, false)

        rootView.swBattery.setOnCheckedChangeListener(this)
        rootView.swTemperature.setOnCheckedChangeListener(this)
        rootView.swTheft.setOnCheckedChangeListener(this)
        rootView.rbBatteryLevel.setOnRangeBarChangeListener(this)
        rootView.rbTemp.setOnRangeBarChangeListener(this)
        rootView.clSendFeedback.setOnClickListener(this)

        BatteryLiveData(context!!).observe(this, Observer<BatteryStatsPoJo> { connection ->
            rootView.cpBattery.isShowTextWhileSpinning = true
            rootView.cpBattery.setValueAnimated(connection!!.level.toFloat())
            rootView.batteryStatus.text = connection.status
            rootView.temperature.text = connection.temp.toString()
        })

        rootView.swBattery.isChecked = AppPreferences.batteryAlarmStatus == true
        rootView.swTemperature.isChecked = AppPreferences.temperatureAlarmStatus == true
        rootView.swTheft.isChecked = AppPreferences.theftAlarmStatus == true

        when {
            (AppPreferences.hbl == 0f) && (AppPreferences.lbl == 0f) -> rootView.rbBatteryLevel.setRangePinsByValue(20f, 85f)
            AppPreferences.hbl == 0f -> rootView.rbBatteryLevel.setRangePinsByValue(AppPreferences.lbl!!, 85f)
            AppPreferences.lbl == 0f -> rootView.rbBatteryLevel.setRangePinsByValue(20f, AppPreferences.hbl!!)
            else -> rootView.rbBatteryLevel.setRangePinsByValue(AppPreferences.lbl!!, AppPreferences.hbl!!)
        }

        when {
            AppPreferences.temp == 0f -> rootView.rbTemp.setSeekPinByValue(35f)
            else -> rootView.rbTemp.setSeekPinByValue(AppPreferences.temp!!)
        }

        return rootView
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when (p0!!.id) {
            R.id.swBattery -> {
                AppPreferences.batteryAlarmStatus = p1
            }
            R.id.swTemperature -> {
                AppPreferences.temperatureAlarmStatus = p1
            }
            R.id.swTheft -> {
                AppPreferences.theftAlarmStatus = p1
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.clSendFeedback -> Utils.sendFeedback(mActivity)
        }
    }

    override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
        when (rangeBar!!.id) {
            R.id.rbBatteryLevel -> {
                if (AppPreferences.hbl != rightPinValue!!.toFloat())
                    AppPreferences.hbl = rightPinValue.toFloat()
                if (AppPreferences.lbl != leftPinValue!!.toFloat())
                    AppPreferences.lbl = leftPinValue.toFloat()
            }
            R.id.rbTemp -> {
                if (AppPreferences.temp != rightPinValue!!.toFloat())
                    AppPreferences.temp = rightPinValue.toFloat()
            }
        }
    }

}
