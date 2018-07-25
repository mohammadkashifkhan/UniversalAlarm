package com.mdkashif.alarm.time

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.home.HomeActivity
import com.mdkashif.alarm.alarm.AlarmReceiver
import com.mdkashif.alarm.time.adapters.DaysInWeekAdapter
import com.mdkashif.alarm.time.adapters.HoursInDayAdapter
import com.mdkashif.alarm.time.adapters.MinutesInHourAdapter
import com.mdkashif.alarm.time.adapters.TimeTypeInDayAdapter
import com.mdkashif.alarm.utils.AppDatabase
import com.mdkashif.alarm.utils.RoomInitializer
import kotlinx.android.synthetic.main.fragment_add_time.*
import kotlinx.android.synthetic.main.fragment_add_time.view.*
import java.util.*


class SetTimeFragment : android.app.Fragment(), TimePresenter.TimePresenterCallback {
    private var mActivity: HomeActivity?=null
    private var hours: List<String>? = null
    private var days: List<String>? = null
    private var minutes: List<String>? = null
    private var type: List<String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View =inflater.inflate(R.layout.fragment_add_time, container, false)
        TimePresenter(this).setDataForTimeAndDays()

        view.hourWheel.setOnWheelItemSelectedListener { parent, itemDrawable, position ->
            Toast.makeText(activity, hours?.get(position)+"", Toast.LENGTH_LONG).show()
        }

        view.minuteWheel.setOnWheelItemSelectedListener { parent, itemDrawable, position ->
            Toast.makeText(activity, minutes?.get(position)+"", Toast.LENGTH_LONG).show()
        }

        view.typeWheel.setOnWheelItemSelectedListener { parent, itemDrawable, position ->
            Toast.makeText(activity, type?.get(position)+"", Toast.LENGTH_LONG).show()
        }

        view.setAlarmBtn.setOnClickListener{
            setAlarm()
        }

        //RoomInitializer.transactAsync(AppDatabase.getAppDatabase(activity))

        return view
    }

    private fun setAlarm(){
        val dat = Date()//initializes to now
        val cal_alarm = Calendar.getInstance()
        val cal_now = Calendar.getInstance()
        cal_now.time = dat
        cal_alarm.time = dat
        cal_alarm.set(Calendar.HOUR_OF_DAY, 2)
        cal_alarm.set(Calendar.MINUTE, 35)
        cal_alarm.set(Calendar.SECOND, 0)
        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1)
        }
        val intent = Intent(activity,
                AlarmReceiver::class.java)
        val pintent = PendingIntent.getBroadcast(activity, 393, intent, 0)

        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal_alarm.timeInMillis, pintent)
        }
        else if (Build.VERSION.SDK_INT >= 21) {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal_alarm.timeInMillis, pintent)
        }

        Log.d(mActivity!!.tag,"here")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDayslist(days)
        setHour(hours)
        setMinute(minutes)
        setTimeType(type)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mActivity = context as HomeActivity
    }

    override fun onDaysOfWeekSuccess(hours: List<String>?, minutes: List<String>?, type: List<String>?, days: List<String>?) {
        this.days =days
        this.hours =hours
        this.minutes =minutes
        this.type =type
    }

    fun setDayslist(days: List<String>?){
        rvDays.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvDays.adapter = DaysInWeekAdapter(days)
    }

    fun setHour(hours: List<String>?){
        hourWheel.adapter = HoursInDayAdapter(hours,activity)
    }

    fun setMinute(minutes: List<String>?){
        minuteWheel.adapter = MinutesInHourAdapter(minutes,activity)
    }

    fun setTimeType(type: List<String>?){
        typeWheel.adapter = TimeTypeInDayAdapter(type,activity)
    }

}
