package com.mdkashif.universalarm.alarm.time.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.misc.AlarmHelper
import com.mdkashif.universalarm.alarm.misc.AlarmOps
import com.mdkashif.universalarm.alarm.misc.AlarmTypes
import com.mdkashif.universalarm.alarm.misc.model.DaysModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import com.mdkashif.universalarm.alarm.time.TimeHelper
import com.mdkashif.universalarm.base.BaseFragment
import com.mdkashif.universalarm.persistence.RoomHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList


class SetTimeFragment : BaseFragment(), View.OnClickListener, MaterialDayPicker.DayPressedListener {

    private lateinit var rootView: View
    private var selectedDays: MutableList<String> = ArrayList()
    private lateinit var timingsModel: TimingsModel
    private lateinit var selectedHour: String
    private lateinit var selectedMinute: String
    private var daysList: MutableList<DaysModel> = ArrayList()
    private lateinit var timeLeftFromNow: String
    private val disposable = CompositeDisposable()
    private var requestCode: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_set_time, container, false)
        rootView.tvPickTime.setOnClickListener(this)
        rootView.btSaveAlarm.setOnClickListener(this)
        rootView.dpDays.setDayPressedListener(this)

        rootView.tvDubai.text = "Dubai : ${TimeHelper.getDifferentZonedTimes(1)}"
        rootView.tvNewYork.text = "New York : ${TimeHelper.getDifferentZonedTimes(2)}"
        rootView.tvSydney.text = "Sydney : ${TimeHelper.getDifferentZonedTimes(3)}"
        rootView.tvMoscow.text = "Moscow : ${TimeHelper.getDifferentZonedTimes(4)}"
        rootView.tvBrasilia.text = "Brasilia : ${TimeHelper.getDifferentZonedTimes(5)}"
        rootView.tvLondon.text = "London : ${TimeHelper.getDifferentZonedTimes(6)}"
        return rootView
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.tvPickTime -> {
                val mCurrentTime = Calendar.getInstance()
                val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
                val minute = mCurrentTime.get(Calendar.MINUTE)
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(mActivity, TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    this.selectedHour = selectedHour.toString()
                    this.selectedMinute = selectedMinute.toString()
                    rootView.tvPickTime.text = """${this.selectedHour}:${this.selectedMinute}"""

                    disposable.add(TimeHelper.getTimeFromNow(selectedHour, selectedMinute).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<String>() {
                        override fun onError(e: Throwable) {
                            // do nothing
                        }

                        override fun onComplete() {
                            // do nothing
                        }

                        override fun onNext(t: String) {
                            timeLeftFromNow = t
                            rootView.tvTime.text = t
                        }
                    }))

                }, hour, minute, false)
                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
            }

            R.id.btSaveAlarm -> {
                when {
                    selectedDays.isEmpty() -> {
                        if (this.selectedMinute.isNotEmpty()) {
                            requestCode = AlarmHelper.returnPendingIntentUniqueRequestCode(mActivity).toLong()
                            timingsModel = if (rootView.etNote.text.toString().isEmpty())
                                TimingsModel(hour = this.selectedHour, minute = this.selectedMinute, alarmType = AlarmTypes.Time.toString(), status = true, pIntentRequestCode = requestCode)
                            else
                                TimingsModel(hour = this.selectedHour, minute = this.selectedMinute, note = rootView.etNote.text.toString(), alarmType = AlarmTypes.Time.toString(), status = true, pIntentRequestCode = requestCode)
                            doAccordingly()
                        }
                    }
                    else -> {
                        if (this.selectedMinute.isNotEmpty()) {
                            for (day in selectedDays) {
                                val daysModel = DaysModel(repeatDay = day)
                                daysList.add(daysModel)
                            }
                            requestCode = AlarmHelper.returnPendingIntentUniqueRequestCode(mActivity).toLong()
                            timingsModel = if (rootView.etNote.text.toString().isEmpty())
                                TimingsModel(hour = this.selectedHour, minute = this.selectedMinute, alarmType = AlarmTypes.Time.toString(), repeat = true, status = true, repeatDays = daysList, pIntentRequestCode = requestCode)
                            else
                                TimingsModel(hour = this.selectedHour, minute = this.selectedMinute, note = rootView.etNote.text.toString(), alarmType = AlarmTypes.Time.toString(), repeat = true, status = true, repeatDays = daysList, pIntentRequestCode = requestCode)
                            doAccordingly()
                        }
                    }
                }
            }
        }
    }

    override fun onDayPressed(weekday: MaterialDayPicker.Weekday?, isSelected: Boolean) {
        if (isSelected)
            selectedDays.add(weekday!!.toString())
        else
            selectedDays.remove(weekday!!.toString())
    }

    private fun doAccordingly() {
        RoomHelper.transactAmendAsync(mActivity.returnDbInstance(), AlarmOps.Add.toString(), timingsModel)
        AlarmHelper.setAlarm(timingsModel.hour.toInt(), timingsModel.minute.toInt(), requestCode.toInt(), mActivity)
        mActivity.showToast("Alarm set for $timeLeftFromNow from now")
        mActivity.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}