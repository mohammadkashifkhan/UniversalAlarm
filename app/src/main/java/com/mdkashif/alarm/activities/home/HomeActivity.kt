package com.mdkashif.alarm.activities.home

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.SettingsActivity
import com.mdkashif.alarm.alarm.ShowAllAlarmsFragment
import com.mdkashif.alarm.base.BaseActivity
import com.mdkashif.alarm.battery.BatteryReceiver
import com.mdkashif.alarm.battery.SetBatteryLevelFragment
import com.mdkashif.alarm.databinding.ActivityHomeBinding
import com.mdkashif.alarm.location.SetLocationFragment
import com.mdkashif.alarm.prayer.SetPrayerTimeFragment
import com.mdkashif.alarm.time.SetTimeFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_add_location.*
import permissions.dispatcher.*


@RuntimePermissions
class HomeActivity : BaseActivity() {

    private val alarmType: List<String> = listOf("time","battery","location")
    private var binding: ActivityHomeBinding? = null
    private var mBatInfoReceiver: BatteryReceiver?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        when {
            "com.mdkashif.alarm.activities.home.time" == intent.action -> replaceFragment(SetTimeFragment(),HomeActivity::class.java.simpleName,true)
            //"com.mdkashif.alarm.activities.home.battery" == intent.action -> replaceFragment(SetBatteryLevelFragment(),HomeActivity::class.java.simpleName,true)
            "com.mdkashif.alarm.activities.home.location" == intent.action -> replaceFragment(SetLocationFragment(),HomeActivity::class.java.simpleName,true)
            "com.mdkashif.alarm.activities.home.prayer" == intent.action -> replaceFragment(SetPrayerTimeFragment(),HomeActivity::class.java.simpleName,true)
        }

        needsPermissionWithPermissionCheck()

        initiateReceiver()

        setAdapter()

        fab_time.setOnClickListener{
            menu.close(true)
            replaceFragment(SetTimeFragment(),HomeActivity::class.java.simpleName,true)
        }

        fab_battery.setOnClickListener{
            menu.close(true)
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            ft.setCustomAnimations(R.animator.slide_up, 0, 0, R.animator.slide_down)
            ft.replace(R.id.fl_main_content, SetBatteryLevelFragment(), HomeActivity::class.java.simpleName)
                ft.addToBackStack(HomeActivity::class.java.simpleName)

            ft.commit()
        }

        fab_location.setOnClickListener{
            menu.close(true)
            replaceFragment(SetLocationFragment(),HomeActivity::class.java.simpleName,true)
        }

        fab_salat.setOnClickListener{
            menu.close(true)
//            Log.d("check", city)
            if (!(isBlank(city) && (isBlank(country))))
                replaceFragment(SetPrayerTimeFragment(),HomeActivity::class.java.simpleName,true)
            else
                showSnackbar("Please try after some time", this)
        }

        ivSettings.setOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        tvSeeAll.setOnClickListener{
            replaceFragment(ShowAllAlarmsFragment(),HomeActivity::class.java.simpleName,true)
        }

    }

    fun initiateReceiver(){
        mBatInfoReceiver= BatteryReceiver.getInstance()
        registerReceiver(mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    private fun setAdapter() {
        rvAlarms.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val defaultPadding = 20
        rvAlarms.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                val position = parent.getChildAdapterPosition(view)
                if (position == RecyclerView.NO_POSITION) {
                    return
                }
                outRect.top = if (position == 0) defaultPadding / 2 else defaultPadding / 4
                outRect.bottom = if (position == parent.adapter.itemCount - 1) defaultPadding / 2 else defaultPadding / 4
            }
        })
//        AnimationSingleton.getInstance().set_rule_and_emp_Animation(rvAlarms)
        rvAlarms.adapter = HomeAlarmListAdapter(this, alarmType)
    }

    override fun onBackPressed() {
        if(search_view!=null){
            if (search_view.isSearchOpen)
                search_view.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val item = menu!!.findItem(R.id.action_search)
        search_view?.setMenuItem(item)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId==R.id.action_search)
                return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        unregisterReceiver(mBatInfoReceiver)
        super.onDestroy()
    }


    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun needsPermission() {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun OnShowRationale(request: PermissionRequest) {
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun OnPermissionDenied() {
    }
}