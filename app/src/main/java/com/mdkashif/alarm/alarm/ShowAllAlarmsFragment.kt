package com.mdkashif.alarm.alarm

import android.app.Fragment
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.home.HomeAlarmListAdapter
import com.mdkashif.alarm.utils.AnimationSingleton
import kotlinx.android.synthetic.main.fragment_show_all_alarms.*

class ShowAllAlarmsFragment : Fragment() {
    val alarmType: List<String> = listOf("time","battery","location","time","time","location","battery","location")

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
        val defaultPadding = 20
        rvAlarms.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                val position = parent.getChildAdapterPosition(view)
                if (position == RecyclerView.NO_POSITION) {
                    return
                }
                outRect.top = if (position == 0) defaultPadding / 2 else defaultPadding / 4
                outRect.bottom = if (position == parent.adapter.itemCount - 1) defaultPadding / 2 else defaultPadding / 4
            }
        })
        AnimationSingleton.set_alarms_Animation(rvAlarms)
        rvAlarms.adapter = HomeAlarmListAdapter(activity, alarmType)
    }

}
