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
import com.mdkashif.universalarm.persistence.RoomRepository
import com.mdkashif.universalarm.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_buzzing_alarm.view.*
import kotlinx.android.synthetic.main.fragment_set_time.view.*
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
    private lateinit var mTimePicker: TimePickerDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_set_time, container, false)
        rootView.tvPickTime.setOnClickListener(this)
        rootView.btSetAlarm.setOnClickListener(this)
        rootView.dpDays.setDayPressedListener(this)
        rootView.clSendFeedback.setOnClickListener(this)

        rootView.tvDubai.text = "Dubai : ${TimeHelper.getDifferentZonedTimes(1)}"
        rootView.tvNewYork.text = "New York : ${TimeHelper.getDifferentZonedTimes(2)}"
        rootView.tvSydney.text = "Sydney : ${TimeHelper.getDifferentZonedTimes(3)}"
        rootView.tvMoscow.text = "Moscow : ${TimeHelper.getDifferentZonedTimes(4)}"
        rootView.tvBrasilia.text = "Brasilia : ${TimeHelper.getDifferentZonedTimes(5)}"
        rootView.tvLondon.text = "London : ${TimeHelper.getDifferentZonedTimes(6)}"

        when {
            arguments != null -> {
                timingsModel = arguments!!.getParcelable("editableData") as TimingsModel
                rootView.tvPickTime.text = "${timingsModel.hour}:${timingsModel.minute}"

                disposable.add(TimeHelper.getTimeFromNow(timingsModel.hour.toInt(), timingsModel.minute.toInt()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<String>() {
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
                if (timingsModel.repeat) {
                    for (i in timingsModel.repeatDays!!.indices) {
                        selectedDays.add(timingsModel.repeatDays!![i].repeatDay)
                        rootView.dpDays.setSelectedDays(MaterialDayPicker.Weekday.valueOf(timingsModel.repeatDays!![i].repeatDay))
                    }
                }
                rootView.tvNote.text = timingsModel.note
            }
        }
        return rootView
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.tvPickTime -> {
                val mCurrentTime = Calendar.getInstance()
                val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
                val minute = mCurrentTime.get(Calendar.MINUTE)
                mTimePicker = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    this.selectedHour = selectedHour.toString()
                    this.selectedMinute = selectedMinute.toString()
                    rootView.tvPickTime.text = "${this.selectedHour}:${this.selectedMinute}"

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

            R.id.btSetAlarm -> {
                when {
                    selectedDays.isEmpty() -> {
                        if (this.selectedMinute.isNotEmpty()) {
                            requestCode = AlarmHelper.returnPendingIntentUniqueRequestCode().toLong()
                            timingsModel = when {
                                rootView.etNote.text.toString().isEmpty() -> TimingsModel(hour = this.selectedHour, minute = this.selectedMinute, alarmType = AlarmTypes.Time.toString(), status = true, pIntentRequestCode = requestCode)
                                else -> TimingsModel(hour = this.selectedHour, minute = this.selectedMinute, note = rootView.etNote.text.toString(), alarmType = AlarmTypes.Time.toString(), status = true, pIntentRequestCode = requestCode)
                            }
                            doAccordingly()
                        }
                    }
                    else -> {
                        if (this.selectedMinute.isNotEmpty()) {
                            for (day in selectedDays) {
                                val daysModel = DaysModel(repeatDay = day)
                                daysList.add(daysModel)
                            }
                            requestCode = AlarmHelper.returnPendingIntentUniqueRequestCode().toLong()
                            timingsModel = when {
                                rootView.etNote.text.toString().isEmpty() -> TimingsModel(hour = this.selectedHour, minute = this.selectedMinute, alarmType = AlarmTypes.Time.toString(), repeat = true, status = true, repeatDays = daysList, pIntentRequestCode = requestCode)
                                else -> TimingsModel(hour = this.selectedHour, minute = this.selectedMinute, note = rootView.etNote.text.toString(), alarmType = AlarmTypes.Time.toString(), repeat = true, status = true, repeatDays = daysList, pIntentRequestCode = requestCode)
                            }
                            doAccordingly()
                        }
                    }
                }
            }

            R.id.clSendFeedback -> Utils.sendFeedback(mActivity)
        }
    }

    override fun onDayPressed(weekday: MaterialDayPicker.Weekday?, isSelected: Boolean) {
        when {
            isSelected -> selectedDays.add(weekday!!.toString())
            else -> selectedDays.remove(weekday!!.toString())
        }
    }

    private fun doAccordingly() {
        if ((arguments!!.getParcelable("editableData") as TimingsModel) == null)
            RoomRepository.amendTimingsAsync(mActivity.returnDbInstance(), AlarmOps.Add.toString(), timingsModel)
        else {
            AlarmHelper.stopAlarm(timingsModel.pIntentRequestCode.toInt(), mActivity) // removing older pIntents
            RoomRepository.amendTimingsAsync(mActivity.returnDbInstance(), AlarmOps.Update.toString(), timingsModel, timingsModel.id)
        }
        when {
            daysList.isNotEmpty() -> AlarmHelper.setAlarm(timingsModel.hour.toInt(), timingsModel.minute.toInt(), requestCode.toInt(), mActivity, AlarmTypes.Time, timingsModel.note, repeat = true, repeatDays = daysList)
            else -> AlarmHelper.setAlarm(timingsModel.hour.toInt(), timingsModel.minute.toInt(), requestCode.toInt(), mActivity, AlarmTypes.Time, timingsModel.note, repeatDays = null)
        }
        Utils.showToast("Alarm set for $timeLeftFromNow from now", mActivity)
        mActivity.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}