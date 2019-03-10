package com.mdkashif.universalarm.alarm.misc.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.SettingsActivity
import com.mdkashif.universalarm.alarm.battery.ui.SetBatteryLevelFragment
import com.mdkashif.universalarm.alarm.location.ui.SetLocationFragment
import com.mdkashif.universalarm.alarm.misc.AlarmTypes
import com.mdkashif.universalarm.alarm.misc.AlarmsListAdapter
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import com.mdkashif.universalarm.alarm.prayer.ui.SetPrayerTimeFragment
import com.mdkashif.universalarm.alarm.time.ui.SetTimeFragment
import com.mdkashif.universalarm.base.BaseFragment
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.persistence.RoomHelper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : BaseFragment(), View.OnClickListener {
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var rootView: View
    private lateinit var timingsList: MutableList<TimingsModel>
    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        rootView.fabTime.setOnClickListener(this)
        rootView.fabBattery.setOnClickListener(this)
        rootView.fabLocation.setOnClickListener(this)
        rootView.fabSalat.setOnClickListener(this)
        rootView.ivSettings.setOnClickListener(this)
        rootView.tvSeeAll.setOnClickListener(this)

        return rootView
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.fabTime -> {
                rootView.menu.close(true)
                mActivity.replaceFragment(SetTimeFragment(), SetTimeFragment::class.java.simpleName, true)
            }
            R.id.fabBattery -> {
                rootView.menu.close(true)
                mActivity.replaceFragment(SetBatteryLevelFragment(), SetBatteryLevelFragment::class.java.simpleName, true)
            }
            R.id.fabLocation -> {
                rootView.menu.close(true)
                mActivity.replaceFragment(SetLocationFragment(), SetLocationFragment::class.java.simpleName, true)
            }
            R.id.fabSalat -> {
                rootView.menu.close(true)
                if (AppPreferences.islamicDate != "")
                    mActivity.replaceFragment(SetPrayerTimeFragment(), SetPrayerTimeFragment::class.java.simpleName, true)
                else
                    mActivity.showSnackBar("Fetching the latest Prayer timings, Please try again later")
            }
            R.id.ivSettings -> {
                mActivity.executeIntent(Intent(context, SettingsActivity::class.java), false)
            }
            R.id.tvSeeAll -> {
                mActivity.replaceFragment(ShowAllAlarmsFragment(), ShowAllAlarmsFragment::class.java.simpleName, true)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pair = RoomHelper.transactFetchAsync(mActivity.returnDbInstance(), AlarmTypes.Time)
        timingsList = pair.first // Pair's first value
        pair.second.observe(this, Observer<Int> {
            if (AppPreferences.hbl != 0f)
                rootView.tvSeeAll.text = "+${it - 3} more" // adding battery count
            else
                rootView.tvSeeAll.text = "+${it - 4} more"
        })
        setRVAdapter(timingsList)
        if (pair.first.isEmpty())
            rootView.tvSeeAll.visibility = View.INVISIBLE
    }

    private fun setRVAdapter(timingsList: MutableList<TimingsModel>) {
        mLinearLayoutManager = LinearLayoutManager(mActivity)
        rvAlarms.layoutManager = mLinearLayoutManager
        mActivity.setRVSlideInLeftAnimation(rvAlarms)
        val adapter = AlarmsListAdapter(timingsList, "Home", mActivity, mLinearLayoutManager, disposable)
        rvAlarms.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}
