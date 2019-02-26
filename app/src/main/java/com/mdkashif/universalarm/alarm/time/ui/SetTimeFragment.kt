package com.mdkashif.universalarm.alarm.time.ui

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.ContainerActivity
import com.mdkashif.universalarm.alarm.time.TimePresenter
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder
import kotlinx.android.synthetic.main.fragment_set_time.view.*
import java.util.*
import kotlin.collections.ArrayList


class SetTimeFragment : Fragment(), View.OnClickListener, MaterialDayPicker.DayPressedListener {

    private lateinit var mActivity: ContainerActivity
    private lateinit var rootView: View
    private var selectedDays : MutableList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_set_time, container, false)
        rootView.tvPickTime.setOnClickListener(this)
        rootView.btSaveAlarm.setOnClickListener(this)
        rootView.tvSnooze.setOnClickListener(this)
        rootView.dpDays.setOnClickListener(this)

        rootView.tvDubai.text = """Dubai : ${TimePresenter.getDifferentZonedTimes(1)}"""
        rootView.tvNewYork.text = """New York : ${TimePresenter.getDifferentZonedTimes(2)}"""
        rootView.tvSydney.text = """Sydney : ${TimePresenter.getDifferentZonedTimes(3)}"""
        rootView.tvMoscow.text = """Moscow : ${TimePresenter.getDifferentZonedTimes(4)}"""
        rootView.tvBrasilia.text = """Brasilia : ${TimePresenter.getDifferentZonedTimes(5)}"""
        rootView.tvLondon.text = """London : ${TimePresenter.getDifferentZonedTimes(6)}"""
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mActivity = context as ContainerActivity
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.tvPickTime -> {
                val mCurrentTime = Calendar.getInstance()
                val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
                val minute = mCurrentTime.get(Calendar.MINUTE)
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(mActivity, TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    rootView.tvPickTime.text = "$selectedHour:$selectedMinute"
                    rootView.tvTime.text = TimePresenter.calculateTimeFromNow(selectedHour, selectedMinute)
                }, hour, minute, false)
                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
            }

            R.id.tvSnooze -> {
                MaterialDialog(mActivity).show {
                    title(R.string.snoozeTitle)
                    listItemsSingleChoice(R.array.snoozeTimings) { dialog, index, text ->
                        SharedPrefHolder.getInstance(mActivity).snoozeTimeArrayPosition = index
                        dialog.dismiss()
                    }
                }
            }

            R.id.btSaveAlarm -> {
//                if(selectedDays.isEmpty()){
//                    var timingsModel = TimingsModel(day = "20", month = "02", year = "2019", hour = "6", minute = "15", alarmType = AlarmTypes.Fajr.toString())
//                    RoomHelper.transactAmendAsync(mActivity.appDatabase, AlarmOps.Add.toString(), timingsModel)}
//                else{
//                    var timingsModel1 = TimingsModel(day = "21", month = "03", year = "2020", hour = "8", minute = "30", alarmType = AlarmTypes.Time.toString(), repeat = true, repeatDays = listOf(DaysModel(repeatDay = "m"), DaysModel(repeatDay = "w"), DaysModel(repeatDay = "f")))
//                    RoomHelper.transactAmendAsync(mActivity.appDatabase, AlarmOps.Add.toString(), timingsModel1)}
            }
        }
    }

    override fun onDayPressed(weekday: MaterialDayPicker.Weekday?, isSelected: Boolean) {
        if(isSelected)
            selectedDays.add(weekday!!.toString())
        else
            selectedDays.remove(weekday!!.toString())
    }
}
