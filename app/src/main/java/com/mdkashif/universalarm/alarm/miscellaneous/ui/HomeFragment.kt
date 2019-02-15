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

    private val alarmType: MutableList<String> = mutableListOf(AlarmTypes.Time.toString(), AlarmTypes.Battery.toString(), AlarmTypes.Prayer.toString(), AlarmTypes.Location.toString())
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var rootView: View
    private lateinit var mActivity: ContainerActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        rootView.fab_time.setOnClickListener(this)
        rootView.fab_battery.setOnClickListener(this)
        rootView.fab_location.setOnClickListener(this)
        rootView.fab_salat.setOnClickListener(this)
        rootView.ivSettings.setOnClickListener(this)
        rootView.tvSeeAll.setOnClickListener(this)

        setRVAdapter(rootView.rvAlarms)

//        var timingsModel= TimingsModel(0, "20","07","1993","7", "15", "prayer", false, null)
//        RoomHelper.transactAmendAsync(mActivity.returnDbInstance(),"add", timingsModel)
//
//        var timingsModel1= TimingsModel(0, "20","07","1993","8", "30", "generic", true, listOf(DaysModel(0,1,"m"),DaysModel(0,1,"w"),DaysModel(0,1,"f")))
//        RoomHelper.transactAmendAsync(mActivity.returnDbInstance(),"add", timingsModel1)
//        var count = RoomHelper.transactCountAsync(mActivity.returnDbInstance())
//
//        Log.d("check132",""+count)

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as ContainerActivity
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.fab_time -> {
                rootView.menu.close(true)
                mActivity.replaceFragment(SetTimeFragment(), SetTimeFragment::class.java.simpleName, true)
            }
            R.id.fab_battery -> {
                rootView.menu.close(true)
                mActivity.replaceFragment(SetBatteryLevelFragment(), SetBatteryLevelFragment::class.java.simpleName, true)
            }
            R.id.fab_location -> {
                rootView.menu.close(true)
                mActivity.replaceFragment(SetLocationFragment(), SetLocationFragment::class.java.simpleName, true)
            }
            R.id.fab_salat -> {
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
