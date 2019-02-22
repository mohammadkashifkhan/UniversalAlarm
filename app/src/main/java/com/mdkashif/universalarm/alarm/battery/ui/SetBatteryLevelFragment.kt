package com.mdkashif.universalarm.alarm.battery.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.appyvet.materialrangebar.RangeBar
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.ContainerActivity
import com.mdkashif.universalarm.alarm.battery.misc.BatteryLiveData
import com.mdkashif.universalarm.alarm.battery.misc.BatteryStatsPoJo
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder
import kotlinx.android.synthetic.main.fragment_set_battery_level.*
import kotlinx.android.synthetic.main.fragment_set_battery_level.view.*


class SetBatteryLevelFragment : Fragment(), CompoundButton.OnCheckedChangeListener, RangeBar.OnRangeBarChangeListener {

    private lateinit var mActivity: ContainerActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var rootView : View =inflater.inflate(R.layout.fragment_set_battery_level, container, false)

        rootView.swBattery.setOnCheckedChangeListener(this)
        rootView.swTemperature.setOnCheckedChangeListener(this)
        rootView.swTheft.setOnCheckedChangeListener(this)
        rootView.rbBatteryLevel.setOnRangeBarChangeListener(this)
        rootView.rbTemp.setOnRangeBarChangeListener(this)

        BatteryLiveData(context!!).observe(this, Observer<BatteryStatsPoJo> { connection ->
            batteryChangeMeter.isShowTextWhileSpinning = true
            batteryChangeMeter.setValueAnimated(connection!!.level.toFloat())
            batteryStatus.text = connection.status
            temperature.text = connection.temp.toString()
        })

        rootView.swBattery.isChecked = SharedPrefHolder.getInstance(activity).batteryAlarmStatus == true
        rootView.swTemperature.isChecked = SharedPrefHolder.getInstance(activity).temperatureAlarmStatus == true
        rootView.swTheft.isChecked = SharedPrefHolder.getInstance(activity).theftAlarmStatus == true

        if((SharedPrefHolder.getInstance(activity).hbl == 0f) && (SharedPrefHolder.getInstance(activity).lbl == 0f))
            rootView.rbBatteryLevel.setRangePinsByValue(20f,85f)
        else if (SharedPrefHolder.getInstance(activity).hbl == 0f)
            rootView.rbBatteryLevel.setRangePinsByValue(SharedPrefHolder.getInstance(activity).lbl,85f)
        else if (SharedPrefHolder.getInstance(activity).lbl == 0f)
            rootView.rbBatteryLevel.setRangePinsByValue(20f, SharedPrefHolder.getInstance(activity).hbl)
        else
            rootView.rbBatteryLevel.setRangePinsByValue(SharedPrefHolder.getInstance(activity).lbl, SharedPrefHolder.getInstance(activity).hbl)


        if(SharedPrefHolder.getInstance(activity).temp == 0f)
            rootView.rbTemp.setSeekPinByValue(35f)
        else
            rootView.rbTemp.setSeekPinByValue(SharedPrefHolder.getInstance(activity).temp)

        return rootView
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when(p0!!.id){
            R.id.swBattery->{
                SharedPrefHolder.getInstance(activity).batteryAlarmStatus = p1
            }
            R.id.swTemperature->{
                SharedPrefHolder.getInstance(activity).temperatureAlarmStatus = p1
            }
            R.id.swTheft->{
                SharedPrefHolder.getInstance(activity).theftAlarmStatus = p1
            }
        }
    }

    override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
        when(rangeBar!!.id){
            R.id.rbBatteryLevel->{
                if(SharedPrefHolder.getInstance(activity).hbl != rightPinValue!!.toFloat())
                    SharedPrefHolder.getInstance(activity).hbl = rightPinValue.toFloat()
                if(SharedPrefHolder.getInstance(activity).lbl != leftPinValue!!.toFloat())
                    SharedPrefHolder.getInstance(activity).lbl = leftPinValue.toFloat()
            }
            R.id.rbTemp->{
                if(SharedPrefHolder.getInstance(activity).temp != rightPinValue!!.toFloat())
                    SharedPrefHolder.getInstance(activity).temp = rightPinValue.toFloat()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mActivity = context as ContainerActivity
    }

}
