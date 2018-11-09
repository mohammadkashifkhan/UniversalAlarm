package com.mdkashif.alarm.activities

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdkashif.alarm.R
import com.mdkashif.alarm.alarm.AlarmListAdapter
import com.mdkashif.alarm.alarm.battery.BatteryReceiver
import com.mdkashif.alarm.alarm.battery.SetBatteryLevelFragment
import com.mdkashif.alarm.alarm.location.SetLocationFragment
import com.mdkashif.alarm.alarm.miscellaneous.ShowAllAlarmsFragment
import com.mdkashif.alarm.alarm.prayer.SetPrayerTimeFragment
import com.mdkashif.alarm.alarm.time.SetTimeFragment
import com.mdkashif.alarm.databinding.ActivityHomeBinding
import kotlinx.android.synthetic.main.activity_home.*
import permissions.dispatcher.*

@RuntimePermissions
class HomeActivity : BaseActivity() {

    private val alarmType: List<String> = listOf("time","battery","prayer")
    private var binding: ActivityHomeBinding? = null
    private var mBatInfoReceiver: BatteryReceiver?=null
    var mlinearLayoutManager : LinearLayoutManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        when {
            "com.mdkashif.alarm.activities.time" == intent.action -> replaceFragment(SetTimeFragment(), HomeActivity::class.java.simpleName,true)
            "com.mdkashif.alarm.activities.battery" == intent.action -> replaceFragment(SetBatteryLevelFragment(),HomeActivity::class.java.simpleName,true)
            "com.mdkashif.alarm.activities.location" == intent.action -> replaceFragment(SetLocationFragment(), HomeActivity::class.java.simpleName,true)
            "com.mdkashif.alarm.activities.prayer" == intent.action -> replaceFragment(SetPrayerTimeFragment(), HomeActivity::class.java.simpleName,true)
        }

        needsPermissionWithPermissionCheck()

        initiateReceiver()

        setAdapter()

        fab_time.setOnClickListener{
            menu.close(true)
            replaceFragment(SetTimeFragment(), HomeActivity::class.java.simpleName,true)
        }

        fab_battery.setOnClickListener{
            menu.close(true)
            replaceFragment(SetBatteryLevelFragment(), HomeActivity::class.java.simpleName,true)
        }

        fab_location.setOnClickListener{
            menu.close(true)
            replaceFragment(SetLocationFragment(), HomeActivity::class.java.simpleName,true)
        }

        fab_salat.setOnClickListener{
            menu.close(true)
//            Log.d("check", city)
            if (!(isBlank(city) && (isBlank(country))))
                replaceFragment(SetPrayerTimeFragment(), HomeActivity::class.java.simpleName,true)
            else
                showSnackbar("Please try after some time", this)
        }

        ivSettings.setOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

        tvSeeAll.setOnClickListener{
            replaceFragment(ShowAllAlarmsFragment(), HomeActivity::class.java.simpleName,true)
        }

    }

    fun initiateReceiver(){
        mBatInfoReceiver= BatteryReceiver.getInstance()
        registerReceiver(mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    private fun setAdapter() {
        mlinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvAlarms.layoutManager=mlinearLayoutManager
        setRVSlideInLeftAnimation(rvAlarms)
        rvAlarms.adapter = AlarmListAdapter(mlinearLayoutManager!!, alarmType)
    }

    override fun onBackPressed() {
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

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