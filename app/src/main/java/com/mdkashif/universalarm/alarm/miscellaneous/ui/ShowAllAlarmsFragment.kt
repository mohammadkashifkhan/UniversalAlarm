package com.mdkashif.universalarm.alarm.miscellaneous.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmTypes
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmsListAdapter
import com.mdkashif.universalarm.alarm.miscellaneous.model.TimingsModel
import com.mdkashif.universalarm.base.BaseFragment
import com.mdkashif.universalarm.utils.persistence.RoomHelper
import kotlinx.android.synthetic.main.fragment_show_all_alarms.*

class ShowAllAlarmsFragment : BaseFragment() {
    private var mLinearLayoutManager : LinearLayoutManager?=null
    private var timingsList: MutableList<TimingsModel> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var rootView: View =inflater.inflate(R.layout.fragment_show_all_alarms, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timingsList = RoomHelper.transactFetchAsync(mActivity.returnDbInstance(), AlarmTypes.Time).first // Pair's first value
        setRVAdapter(timingsList)
    }

    private fun setRVAdapter(timingsList: MutableList<TimingsModel>) {
        mLinearLayoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
        rvAlarms.layoutManager=mLinearLayoutManager
        mActivity.setRVSlideInLeftAnimation(rvAlarms)
        val adapter = AlarmsListAdapter(timingsList, "ShowAll", context!!)
        rvAlarms.adapter = adapter
        mActivity.enableSwipeToDeleteAndUndo(adapter, rvAlarms)
    }

}
