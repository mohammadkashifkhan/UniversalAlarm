package com.mdkashif.alarm.alarm.time.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.ContainerActivity
import com.mdkashif.alarm.alarm.time.misc.*
import kotlinx.android.synthetic.main.fragment_add_time.*
import kotlinx.android.synthetic.main.fragment_add_time.view.*


class SetTimeFragment : Fragment(), TimePresenter.TimePresenterCallback {
    private lateinit var mActivity: ContainerActivity
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

        view.btSetAlarm.setOnClickListener{
            // TODO: insert in Db
            //RoomHelper.transactAsync(AppDatabase.getAppDatabase(activity))
        }

        return view
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
        this.mActivity = context as ContainerActivity
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
