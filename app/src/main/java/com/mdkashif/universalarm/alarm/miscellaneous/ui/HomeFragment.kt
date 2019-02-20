package com.mdkashif.universalarm.alarm.miscellaneous.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.ContainerActivity
import com.mdkashif.universalarm.activities.SettingsActivity
import com.mdkashif.universalarm.alarm.battery.ui.SetBatteryLevelFragment
import com.mdkashif.universalarm.alarm.location.ui.SetLocationFragment
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmListAdapter
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmTypes
import com.mdkashif.universalarm.alarm.prayer.ui.SetPrayerTimeFragment
import com.mdkashif.universalarm.alarm.time.ui.SetTimeFragment
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(), View.OnClickListener {

    private val alarmType: MutableList<String> = mutableListOf(AlarmTypes.Time.toString(), AlarmTypes.Battery.toString(), AlarmTypes.Asr.toString(), AlarmTypes.Location.toString())
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var rootView: View
    private lateinit var mActivity: ContainerActivity

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

//        var timingsModel = TimingsModel(day = "20", month = "02", year = "2019", hour = "6", minute = "15", alarmType = AlarmTypes.Fajr.toString())
//        RoomHelper.transactAmendAsync(mActivity.returnDbInstance(), AlarmOps.Add.toString(), timingsModel)
//
//        var timingsModel1 = TimingsModel(day = "21", month = "03", year = "2020", hour = "8", minute = "30", alarmType = AlarmTypes.Time.toString(), repeat = true, repeatDays = listOf(DaysModel(repeatDay = "m"), DaysModel(repeatDay = "w"), DaysModel(repeatDay = "f")))
//        RoomHelper.transactAmendAsync(mActivity.returnDbInstance(), AlarmOps.Add.toString(), timingsModel1)
//
//        var timingsModel2 = TimingsModel(day = "22", month = "04", year = "2021", hour = "9", minute = "45", alarmType = AlarmTypes.Asr.toString(), repeat = true, repeatDays = listOf(DaysModel(repeatDay = "t"), DaysModel(repeatDay = "t")))
//        RoomHelper.transactAmendAsync(mActivity.returnDbInstance(), AlarmOps.Add.toString(), timingsModel2)

//        var timingsModel3 = TimingsModel(day = "23", month = "05", year = "2022", hour = "10", minute = "00", alarmType = AlarmTypes.Fajr.toString(), repeat = true, repeatDays = listOf(DaysModel(repeatDay = "s")))
//        RoomHelper.transactAmendAsync(mActivity.returnDbInstance(), AlarmOps.Add.toString(), timingsModel3)
//        RoomHelper.transactCountAsync(mActivity.returnDbInstance())

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as ContainerActivity
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
                if (mActivity.isOnline)
                    mActivity.replaceFragment(SetPrayerTimeFragment(), SetPrayerTimeFragment::class.java.simpleName, true)
                else
                    mActivity.showSnackBar("Please try after some time")
            }
            R.id.ivSettings -> {
                mActivity.startActivity(Intent(context, SettingsActivity::class.java))
                mActivity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
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
