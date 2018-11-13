package com.mdkashif.alarm.alarm.miscellaneous

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hendraanggrian.widget.ExpandableRecyclerView
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.BaseActivity.Companion.city
import com.mdkashif.alarm.activities.BaseActivity.Companion.country
import com.mdkashif.alarm.activities.ContainerActivity
import com.mdkashif.alarm.activities.SettingsActivity
import com.mdkashif.alarm.alarm.AlarmListAdapter
import com.mdkashif.alarm.alarm.battery.SetBatteryLevelFragment
import com.mdkashif.alarm.alarm.location.SetLocationFragment
import com.mdkashif.alarm.alarm.prayer.SetPrayerTimeFragment
import com.mdkashif.alarm.alarm.time.SetTimeFragment
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
    private val alarmType: List<String> = listOf("time","battery","prayer")
    lateinit var mLinearLayoutManager : LinearLayoutManager
    lateinit var rootView: View
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
            if (!(mActivity.isBlank(city) && mActivity.isBlank(country)))
                mActivity.replaceFragment(SetPrayerTimeFragment(), SetPrayerTimeFragment::class.java.simpleName,true)
            else
                mActivity.showSnackbar("Please try after some time")
        }

        rootView.ivSettings.setOnClickListener{
            mActivity.startActivity(Intent(context, SettingsActivity::class.java))
            mActivity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

        rootView.tvSeeAll.setOnClickListener{
            mActivity.replaceFragment(ShowAllAlarmsFragment(), ShowAllAlarmsFragment::class.java.simpleName,true)
        }

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as ContainerActivity
    }

    private fun setRVAdapter(view: ExpandableRecyclerView) {
        mLinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        view.rvAlarms.layoutManager=mLinearLayoutManager
        mActivity.setRVSlideInLeftAnimation(view.rvAlarms)
        view.rvAlarms.setAdapter(AlarmListAdapter(mLinearLayoutManager, alarmType))
    }

}
