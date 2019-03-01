package com.mdkashif.universalarm.alarm.miscellaneous.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmListAdapter
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmTypes
import com.mdkashif.universalarm.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_show_all_alarms.*

class ShowAllAlarmsFragment : BaseFragment() {
    private val alarmType: MutableList<String> = mutableListOf(AlarmTypes.Time.toString(), AlarmTypes.Battery.toString(), AlarmTypes.Fajr.toString(), AlarmTypes.Dhuhr.toString(), AlarmTypes.Isha.toString(), AlarmTypes.Time.toString())
    private var mLinearLayoutManager : LinearLayoutManager?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var rootView: View =inflater.inflate(R.layout.fragment_show_all_alarms, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter() {
        mLinearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
        rvAlarms.layoutManager=mLinearLayoutManager
        mActivity.setRVSlideInLeftAnimation(rvAlarms)
        val adapter = AlarmListAdapter(alarmType)
        rvAlarms.adapter = adapter
        mActivity.enableSwipeToDeleteAndUndo(adapter, rvAlarms)
    }

}
