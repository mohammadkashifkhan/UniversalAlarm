package com.mdkashif.alarm.activities

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.mdkashif.alarm.R
import com.mdkashif.alarm.alarm.battery.misc.BatteryReceiver
import com.mdkashif.alarm.alarm.battery.ui.SetBatteryLevelFragment
import com.mdkashif.alarm.alarm.location.ui.SetLocationFragment
import com.mdkashif.alarm.alarm.miscellaneous.ui.BuzzingAlarmFragment
import com.mdkashif.alarm.alarm.miscellaneous.ui.HomeFragment
import com.mdkashif.alarm.alarm.prayer.ui.SetPrayerTimeFragment
import com.mdkashif.alarm.alarm.time.ui.SetTimeFragment
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
            "BuzzAlarm" == intent.getStringExtra("param")-> replaceFragment(BuzzingAlarmFragment(), BuzzingAlarmFragment::class.java.simpleName,false)
            "com.mdkashif.alarm.activities.time" == intent.action -> replaceFragment(SetTimeFragment(), SetTimeFragment::class.java.simpleName,false)
            "com.mdkashif.alarm.activities.battery" == intent.action -> replaceFragment(SetBatteryLevelFragment(), SetBatteryLevelFragment::class.java.simpleName,false)
            "com.mdkashif.alarm.activities.location" == intent.action -> replaceFragment(SetLocationFragment(), SetLocationFragment::class.java.simpleName,false)
            "com.mdkashif.alarm.activities.prayer" == intent.action -> replaceFragment(SetPrayerTimeFragment(), SetPrayerTimeFragment::class.java.simpleName,false)
            else -> replaceFragment(HomeFragment(), HomeFragment::class.java.simpleName,false)
        }
    }

    private fun initiateReceiver(){
        mBatInfoReceiver= BatteryReceiver.getInstance()
        registerReceiver(mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
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
