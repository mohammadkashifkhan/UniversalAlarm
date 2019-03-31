package com.mdkashif.universalarm.alarm.battery.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import com.appyvet.materialrangebar.RangeBar
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.AntiTheftUnlockActivity
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

        if(AppPreferences().instance.theftPinEnabled!!)
            rootView.btChangePin.visibility=View.VISIBLE

        rootView.swBattery.isChecked = AppPreferences().instance.batteryAlarmStatus == true
        rootView.swTemperature.isChecked = AppPreferences().instance.temperatureAlarmStatus == true
        rootView.swTheft.isChecked = AppPreferences().instance.theftAlarmStatus == true

        when {
            (AppPreferences().instance.hbl == 0f) && (AppPreferences().instance.lbl == 0f) -> rootView.rbBatteryLevel.setRangePinsByValue(20f, 85f)
            AppPreferences().instance.hbl == 0f -> rootView.rbBatteryLevel.setRangePinsByValue(AppPreferences().instance.lbl!!, 85f)
            AppPreferences().instance.lbl == 0f -> rootView.rbBatteryLevel.setRangePinsByValue(20f, AppPreferences().instance.hbl!!)
            else -> rootView.rbBatteryLevel.setRangePinsByValue(AppPreferences().instance.lbl!!, AppPreferences().instance.hbl!!)
        }

        when {
            AppPreferences().instance.temp == 0f -> rootView.rbTemp.setSeekPinByValue(35f)
            else -> rootView.rbTemp.setSeekPinByValue(AppPreferences().instance.temp!!)
        }

        return rootView
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when (p0!!.id) {
            R.id.swBattery -> {
                AppPreferences().instance.batteryAlarmStatus = p1
            }
            R.id.swTemperature -> {
                AppPreferences().instance.temperatureAlarmStatus = p1
            }
            R.id.swTheft -> {
                if (!AppPreferences().instance.theftPinEnabled!!)
                    mActivity.executeIntent(Intent(mActivity, AntiTheftUnlockActivity::class.java), false, param = true, type = "AntiTheftFirstTimeEnable")
                else
                    AppPreferences().instance.theftAlarmStatus = p1
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.clSendFeedback -> Utils.sendFeedback(mActivity)

            R.id.btChangePin -> mActivity.executeIntent(Intent(mActivity, AntiTheftUnlockActivity::class.java), false, param = true, type = "AntiTheftPinChange")
        }
    }

    override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
        when (rangeBar!!.id) {
            R.id.rbBatteryLevel -> {
                if (AppPreferences().instance.hbl != rightPinValue!!.toFloat())
                    AppPreferences().instance.hbl = rightPinValue.toFloat()
                if (AppPreferences().instance.lbl != leftPinValue!!.toFloat())
                    AppPreferences().instance.lbl = leftPinValue.toFloat()
            }
            R.id.rbTemp -> {
                if (AppPreferences().instance.temp != rightPinValue!!.toFloat())
                    AppPreferences().instance.temp = rightPinValue.toFloat()
            }
        }
    }

}
