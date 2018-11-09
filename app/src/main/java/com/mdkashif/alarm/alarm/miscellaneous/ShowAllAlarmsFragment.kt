package com.mdkashif.alarm.alarm.miscellaneous

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.ContainerActivity
import com.mdkashif.alarm.alarm.AlarmListAdapter
import kotlinx.android.synthetic.main.fragment_show_all_alarms.*

class ShowAllAlarmsFragment : Fragment() {
    val alarmType: List<String> = listOf("time","battery","location","prayer","location","time","location")
    var mlinearLayoutManager : LinearLayoutManager?=null

    private var mActivity: ContainerActivity?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View =inflater.inflate(R.layout.fragment_show_all_alarms, container, false)
        return view
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
        mlinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvAlarms.layoutManager=mlinearLayoutManager
        mActivity!!.setRVSlideInLeftAnimation(rvAlarms)
        rvAlarms.adapter = AlarmListAdapter(mlinearLayoutManager!!, alarmType)
    }

}
