package com.mdkashif.universalarm.base


import android.Manifest
import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.github.omadahealth.lollipin.lib.managers.LockManager
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.battery.job.BatteryInfoScheduler
import com.mdkashif.universalarm.alarm.prayer.job.PrayerDataFetchScheduler
import com.mdkashif.universalarm.di.locationHelperModule
import com.mdkashif.universalarm.di.persistenceModule
import com.mdkashif.universalarm.di.retrofitModule
import com.mdkashif.universalarm.misc.ui.AntiTheftUnlockActivity
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import permissions.dispatcher.NeedsPermission

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.P -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME)
        }

        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(listOf(retrofitModule, persistenceModule, locationHelperModule))
        }

        fetchPrayerData()

        BatteryInfoScheduler.scheduleJob(applicationContext) // scheduled it here just for the sake of starting them simultaneously
        // TODO: remove at production!
        Stetho.initializeWithDefaults(this)
        // For AntiTheftUnLockActivity
        val lockManager = LockManager.getInstance()
        lockManager.enableAppLock(this, AntiTheftUnlockActivity::class.java)
        lockManager.appLock.logoId = R.mipmap.ic_launcher_foreground
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun fetchPrayerData(){
        PrayerDataFetchScheduler.scheduleJob(applicationContext)
    }
}
