package com.mdkashif.universalarm.activities

import android.Manifest
import android.os.Bundle
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.battery.ui.SetBatteryLevelFragment
import com.mdkashif.universalarm.alarm.location.ui.SetLocationFragment
import com.mdkashif.universalarm.alarm.miscellaneous.ui.BuzzingAlarmFragment
import com.mdkashif.universalarm.alarm.miscellaneous.ui.HomeFragment
import com.mdkashif.universalarm.alarm.prayer.ui.SetPrayerTimeFragment
import com.mdkashif.universalarm.alarm.time.ui.SetTimeFragment
import com.mdkashif.universalarm.base.BaseActivity
import permissions.dispatcher.*


@RuntimePermissions
class ContainerActivity : BaseActivity() {
//    @Inject lateinit var containerActivity : ContainerActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
//        (application as BaseApplication).getActivityComponent().inject(this)
        getPermissionsWithPermissionCheck()

        when {
            "BuzzAlarm" == intent.getStringExtra("param")-> replaceFragment(BuzzingAlarmFragment(), BuzzingAlarmFragment::class.java.simpleName,false)
            "com.mdkashif.universalarm.activities.time" == intent.action -> replaceFragment(SetTimeFragment(), SetTimeFragment::class.java.simpleName,false)
            "com.mdkashif.universalarm.activities.battery" == intent.action -> replaceFragment(SetBatteryLevelFragment(), SetBatteryLevelFragment::class.java.simpleName,false)
            "com.mdkashif.universalarm.activities.location" == intent.action -> replaceFragment(SetLocationFragment(), SetLocationFragment::class.java.simpleName,false)
            "com.mdkashif.universalarm.activities.prayer" == intent.action -> replaceFragment(SetPrayerTimeFragment(), SetPrayerTimeFragment::class.java.simpleName,false)
            else -> replaceFragment(HomeFragment(), HomeFragment::class.java.simpleName,false)
        }
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
