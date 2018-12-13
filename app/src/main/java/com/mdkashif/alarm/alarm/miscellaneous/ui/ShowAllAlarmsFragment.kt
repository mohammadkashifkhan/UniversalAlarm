package com.mdkashif.alarm.alarm.miscellaneous.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.ContainerActivity
import com.mdkashif.alarm.alarm.miscellaneous.misc.AlarmListAdapter
import kotlinx.android.synthetic.main.fragment_show_all_alarms.*

class ShowAllAlarmsFragment : Fragment() {
    private val alarmType: MutableList<String> = mutableListOf("time","battery","location","prayer","location","time","location")
    private var mLinearLayoutManager : LinearLayoutManager?=null

    private lateinit var mActivity: ContainerActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var rootView: View =inflater.inflate(R.layout.fragment_show_all_alarms, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as ContainerActivity
    }

    private fun setAdapter() {
        mLinearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rvAlarms.layoutManager=mLinearLayoutManager
        mActivity.setRVSlideInLeftAnimation(rvAlarms)
        val adapter = AlarmListAdapter(alarmType)
        rvAlarms.adapter = adapter
        mActivity.enableSwipeToDeleteAndUndo(adapter, rvAlarms)
    }

}
