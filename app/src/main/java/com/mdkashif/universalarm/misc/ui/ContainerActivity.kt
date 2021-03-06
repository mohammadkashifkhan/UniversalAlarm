package com.mdkashif.universalarm.misc.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.battery.ui.SetBatteryLevelFragment
import com.mdkashif.universalarm.alarm.location.ui.SetLocationFragment
import com.mdkashif.universalarm.alarm.misc.view.ui.BuzzingAlarmFragment
import com.mdkashif.universalarm.alarm.misc.view.ui.HomeFragment
import com.mdkashif.universalarm.alarm.prayer.job.PrayerDataFetchScheduler
import com.mdkashif.universalarm.alarm.prayer.ui.SetPrayerTimeFragment
import com.mdkashif.universalarm.alarm.time.ui.SetTimeFragment
import com.mdkashif.universalarm.base.BaseActivity
import permissions.dispatcher.*


@RuntimePermissions
class ContainerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        getPermissionsWithPermissionCheck()

        when {
            "BuzzTimeAlarm" == intent.getStringExtra("param1") -> replaceFragment(BuzzingAlarmFragment(), BuzzingAlarmFragment::class.java.simpleName, false, Intent().getStringExtra("param2").toInt())
            "BuzzLocationAlarm" == intent.getStringExtra("param1") -> replaceFragment(SetLocationFragment(), SetLocationFragment::class.java.simpleName, false, locationDao = Intent().getParcelableExtra("locationDao"))
            "com.mdkashif.universalarm.activities.time" == intent.action -> replaceFragment(SetTimeFragment(), SetTimeFragment::class.java.simpleName, false)
            "com.mdkashif.universalarm.activities.battery" == intent.action -> replaceFragment(SetBatteryLevelFragment(), SetBatteryLevelFragment::class.java.simpleName, false)
            "com.mdkashif.universalarm.activities.location" == intent.action -> replaceFragment(SetLocationFragment(), SetLocationFragment::class.java.simpleName, false)
            "com.mdkashif.universalarm.activities.prayer" == intent.action -> replaceFragment(SetPrayerTimeFragment(), SetPrayerTimeFragment::class.java.simpleName, false)
            else -> replaceFragment(HomeFragment(), HomeFragment::class.java.simpleName, false)
        }
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun getPermissions() {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
        if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            PrayerDataFetchScheduler.scheduleJob(applicationContext)
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
