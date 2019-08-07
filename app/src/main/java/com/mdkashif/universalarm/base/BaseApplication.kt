package com.mdkashif.universalarm.base


import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.github.omadahealth.lollipin.lib.managers.LockManager
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.battery.job.BatteryInfoScheduler
import com.mdkashif.universalarm.di.locationHelperModule
import com.mdkashif.universalarm.di.persistenceModule
import com.mdkashif.universalarm.di.retrofitModule
import com.mdkashif.universalarm.misc.ui.AntiTheftUnlockActivity
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)

        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(listOf(retrofitModule, persistenceModule, locationHelperModule))
        }

        BatteryInfoScheduler.scheduleJob(applicationContext) // scheduled it here just for the sake of starting them simultaneously
        // TODO: remove at production!
        Stetho.initializeWithDefaults(this)
        // For AntiTheftUnLockActivity
        val lockManager = LockManager.getInstance()
        lockManager.enableAppLock(this, AntiTheftUnlockActivity::class.java)
        lockManager.appLock.logoId = R.mipmap.ic_launcher_foreground
    }
}
