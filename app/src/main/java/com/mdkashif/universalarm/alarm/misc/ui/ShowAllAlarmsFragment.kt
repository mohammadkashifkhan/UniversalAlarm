package com.mdkashif.universalarm.alarm.misc.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.misc.AlarmTypes
import com.mdkashif.universalarm.alarm.misc.AlarmsListAdapter
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import com.mdkashif.universalarm.base.BaseFragment
import com.mdkashif.universalarm.persistence.RoomHelper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_show_all_alarms.*

class ShowAllAlarmsFragment : BaseFragment() {
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var timingsList: MutableList<TimingsModel>
    private lateinit var locationsList: MutableList<LocationsModel>
    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_all_alarms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timingsList = RoomHelper.transactFetchAsync(mActivity.returnDbInstance(), AlarmTypes.Time).first // Pair's first value
        locationsList = RoomHelper.transactFetchAsync(mActivity.returnDbInstance(), AlarmTypes.Time).first
        setRVAdapter(timingsList)
    }

    private fun setRVAdapter(timingsList: MutableList<TimingsModel>) {
        mLinearLayoutManager = LinearLayoutManager(mActivity)
        rvAlarms.layoutManager = mLinearLayoutManager
        mActivity.setRVSlideInLeftAnimation(rvAlarms)
        val adapter = AlarmsListAdapter(timingsList, locationsList, "ShowAll", mActivity, mLinearLayoutManager, disposable)
        rvAlarms.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}
