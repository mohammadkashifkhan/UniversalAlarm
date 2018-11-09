package com.mdkashif.alarm.activities

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.mdkashif.alarm.R
import com.mdkashif.alarm.alarm.battery.BatteryReceiver
import com.mdkashif.alarm.alarm.battery.SetBatteryLevelFragment
import com.mdkashif.alarm.alarm.location.SetLocationFragment
import com.mdkashif.alarm.alarm.miscellaneous.HomeFragment
import com.mdkashif.alarm.alarm.prayer.SetPrayerTimeFragment
import com.mdkashif.alarm.alarm.time.SetTimeFragment
import permissions.dispatcher.*

@RuntimePermissions
class ContainerActivity : BaseActivity() {

    private var mBatInfoReceiver: BatteryReceiver?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        getPermissionsWithPermissionCheck()
        initiateReceiver()

        when {
            "com.mdkashif.alarm.activities.time" == intent.action -> replaceFragment(SetTimeFragment(), HomeActivity::class.java.simpleName,true)
            "com.mdkashif.alarm.activities.battery" == intent.action -> replaceFragment(SetBatteryLevelFragment(),HomeActivity::class.java.simpleName,true)
            "com.mdkashif.alarm.activities.location" == intent.action -> replaceFragment(SetLocationFragment(), HomeActivity::class.java.simpleName,true)
            "com.mdkashif.alarm.activities.prayer" == intent.action -> replaceFragment(SetPrayerTimeFragment(), HomeActivity::class.java.simpleName,true)
        }

        replaceFragment(HomeFragment(), HomeFragment::class.java.simpleName,false)
    }

    private fun initiateReceiver(){
        mBatInfoReceiver= BatteryReceiver.getInstance()
        registerReceiver(mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
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
    fun getPermissions() {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onShowRationale(request: PermissionRequest) {
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionDenied() {
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onNeverAskAgain() {
    }
}
