package com.mdkashif.alarm.alarm

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdkashif.alarm.R
import com.mdkashif.alarm.utils.AnimationSingleton
import kotlinx.android.synthetic.main.fragment_show_all_alarms.*

class ShowAllAlarmsFragment : Fragment() {
    val alarmType: List<String> = listOf("time","battery","location","prayer","location","battery")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View =inflater.inflate(R.layout.fragment_show_all_alarms, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter() {
        rvAlarms.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        AnimationSingleton.set_alarms_Animation(rvAlarms)
        rvAlarms.adapter = AlarmListAdapter(activity, alarmType)
    }

}
