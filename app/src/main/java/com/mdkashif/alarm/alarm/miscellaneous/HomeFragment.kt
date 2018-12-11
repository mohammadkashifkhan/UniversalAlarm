package com.mdkashif.alarm.alarm.miscellaneous

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.ContainerActivity
import com.mdkashif.alarm.activities.SettingsActivity
import com.mdkashif.alarm.alarm.battery.SetBatteryLevelFragment
import com.mdkashif.alarm.alarm.location.SetLocationFragment
import com.mdkashif.alarm.alarm.miscellaneous.db.DaysModel
import com.mdkashif.alarm.alarm.miscellaneous.db.TimingsModel
import com.mdkashif.alarm.alarm.prayer.SetPrayerTimeFragment
import com.mdkashif.alarm.alarm.time.SetTimeFragment
import com.mdkashif.alarm.utils.db.RoomHelper
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
    private val alarmType: MutableList<String> = mutableListOf("time","battery","prayer","location")
    private lateinit var mLinearLayoutManager : LinearLayoutManager
    private lateinit var rootView: View
    private lateinit var mActivity: ContainerActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        setRVAdapter(rootView.rvAlarms)

        rootView.fab_time.setOnClickListener{
            rootView.menu.close(true)
            mActivity.replaceFragment(SetTimeFragment(), SetTimeFragment::class.java.simpleName,true)
        }

        rootView.fab_battery.setOnClickListener{
            rootView.menu.close(true)
            mActivity.replaceFragment(SetBatteryLevelFragment(), SetBatteryLevelFragment::class.java.simpleName,true)
        }

        rootView.fab_location.setOnClickListener{
            rootView.menu.close(true)
            mActivity.replaceFragment(SetLocationFragment(), SetLocationFragment::class.java.simpleName,true)
        }

        rootView.fab_salat.setOnClickListener{
            rootView.menu.close(true)
//            Log.d("check", city)
            if (!mActivity.isOnline)
                mActivity.replaceFragment(SetPrayerTimeFragment(), SetPrayerTimeFragment::class.java.simpleName,true)
            else
                mActivity.showSnackBar("Please try after some time")
        }

        rootView.ivSettings.setOnClickListener{
            mActivity.startActivity(Intent(context, SettingsActivity::class.java))
            mActivity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

        rootView.tvSeeAll.setOnClickListener{
            mActivity.replaceFragment(ShowAllAlarmsFragment(), ShowAllAlarmsFragment::class.java.simpleName,true)

            // TODO: Implement this somewhere ;)
//            val intent = Intent(activity, AntiTheftUnlockActivity::class.java)
//            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN)
//            startActivity(intent)
        }

        var timingsModel= TimingsModel(0, "7", "15", "prayer", false, null)
        RoomHelper.transactAmendAsync(mActivity.returnDbInstance(),"add", timingsModel)

        var timingsModel1= TimingsModel(0, "8", "30", "generic", true, listOf(DaysModel(0,1,"m"),DaysModel(0,1,"w"),DaysModel(0,1,"f")))
        RoomHelper.transactAmendAsync(mActivity.returnDbInstance(),"add", timingsModel1)
        var count = RoomHelper.transactFetchAsync(mActivity.returnDbInstance())

        Log.d("check132",""+count)

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as ContainerActivity
    }

    private fun setRVAdapter(view: RecyclerView) {
        mLinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        view.rvAlarms.layoutManager=mLinearLayoutManager
        mActivity.setRVSlideInLeftAnimation(view.rvAlarms)
        val adapter = AlarmListAdapter(alarmType)
        view.rvAlarms.adapter = adapter
        mActivity.enableSwipeToDeleteAndUndo(adapter, view.rvAlarms)
    }

}
