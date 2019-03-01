package com.mdkashif.universalarm.alarm.miscellaneous.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.SettingsActivity
import com.mdkashif.universalarm.alarm.battery.ui.SetBatteryLevelFragment
import com.mdkashif.universalarm.alarm.location.ui.SetLocationFragment
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmListAdapter
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmTypes
import com.mdkashif.universalarm.alarm.prayer.ui.SetPrayerTimeFragment
import com.mdkashif.universalarm.alarm.time.ui.SetTimeFragment
import com.mdkashif.universalarm.base.BaseFragment
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : BaseFragment(), View.OnClickListener {

    private val alarmType: MutableList<String> = mutableListOf(AlarmTypes.Time.toString(), AlarmTypes.Battery.toString(), AlarmTypes.Asr.toString(), AlarmTypes.Location.toString())
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        rootView.fabTime.setOnClickListener(this)
        rootView.fabBattery.setOnClickListener(this)
        rootView.fabLocation.setOnClickListener(this)
        rootView.fabSalat.setOnClickListener(this)
        rootView.ivSettings.setOnClickListener(this)
        rootView.tvSeeAll.setOnClickListener(this)

        setRVAdapter(rootView.rvAlarms)

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
                if (SharedPrefHolder.getInstance(mActivity).islamicDate!="")
                    mActivity.replaceFragment(SetPrayerTimeFragment(), SetPrayerTimeFragment::class.java.simpleName, true)
                else
                    mActivity.showSnackBar("Please try after some time")
            }
            R.id.ivSettings -> {
                mActivity.executeIntent(Intent(context, SettingsActivity::class.java), false)
            }
            R.id.tvSeeAll -> {
                mActivity.replaceFragment(ShowAllAlarmsFragment(), ShowAllAlarmsFragment::class.java.simpleName, true)

                // TODO: Implement this somewhere ;)
//            val intent = Intent(activity, AntiTheftUnlockActivity::class.java)
//            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN)
//            startActivity(intent)
            }
        }
    }

    private fun setRVAdapter(view: RecyclerView) {
        mLinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        view.rvAlarms.layoutManager = mLinearLayoutManager
        mActivity.setRVSlideInLeftAnimation(view.rvAlarms)
        val adapter = AlarmListAdapter(alarmType)
        view.rvAlarms.adapter = adapter
        mActivity.enableSwipeToDeleteAndUndo(adapter, view.rvAlarms)
    }

}
