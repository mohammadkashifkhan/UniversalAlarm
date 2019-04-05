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
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_set_battery_level.view.*
import javax.inject.Inject


class SetBatteryLevelFragment : BaseFragment(), CompoundButton.OnCheckedChangeListener, RangeBar.OnRangeBarChangeListener, View.OnClickListener {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

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

        if (appPreferences.theftPinEnabled!!)
            rootView.btChangePin.visibility = View.VISIBLE

        rootView.swBattery.isChecked = appPreferences.batteryAlarmStatus == true
        rootView.swTemperature.isChecked = appPreferences.temperatureAlarmStatus == true
        rootView.swTheft.isChecked = appPreferences.theftAlarmStatus == true

        when {
            (appPreferences.hbl == 0f) && (appPreferences.lbl == 0f) -> rootView.rbBatteryLevel.setRangePinsByValue(20f, 85f)
            appPreferences.hbl == 0f -> rootView.rbBatteryLevel.setRangePinsByValue(appPreferences.lbl!!, 85f)
            appPreferences.lbl == 0f -> rootView.rbBatteryLevel.setRangePinsByValue(20f, appPreferences.hbl!!)
            else -> rootView.rbBatteryLevel.setRangePinsByValue(appPreferences.lbl!!, appPreferences.hbl!!)
        }

        when {
            appPreferences.temp == 0f -> rootView.rbTemp.setSeekPinByValue(35f)
            else -> rootView.rbTemp.setSeekPinByValue(appPreferences.temp!!)
        }

        return rootView
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when (p0!!.id) {
            R.id.swBattery -> {
                appPreferences.batteryAlarmStatus = p1
            }
            R.id.swTemperature -> {
                appPreferences.temperatureAlarmStatus = p1
            }
            R.id.swTheft -> {
                if (!appPreferences.theftPinEnabled!!)
                    mActivity.executeIntent(Intent(mActivity, AntiTheftUnlockActivity::class.java), false, param = true, type = "AntiTheftFirstTimeEnable")
                else
                    appPreferences.theftAlarmStatus = p1
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
                if (appPreferences.hbl != rightPinValue!!.toFloat())
                    appPreferences.hbl = rightPinValue.toFloat()
                if (appPreferences.lbl != leftPinValue!!.toFloat())
                    appPreferences.lbl = leftPinValue.toFloat()
            }
            R.id.rbTemp -> {
                if (appPreferences.temp != rightPinValue!!.toFloat())
                    appPreferences.temp = rightPinValue.toFloat()
            }
        }
    }

}
