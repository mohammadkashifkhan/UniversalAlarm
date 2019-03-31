package com.mdkashif.universalarm.base


import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.github.omadahealth.lollipin.lib.managers.LockManager
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.AntiTheftUnlockActivity
import com.mdkashif.universalarm.alarm.battery.job.BatteryInfoScheduler
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BaseApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .build().inject(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)

        BatteryInfoScheduler.scheduleJob(applicationContext) // scheduled it here just for the sake of starting them simultaneously
        // TODO: remove at production!
        Stetho.initializeWithDefaults(this)
        // For AntiTheftUnLockActivity
        val lockManager = LockManager.getInstance()
        lockManager.enableAppLock(this, AntiTheftUnlockActivity::class.java)
        lockManager.appLock.logoId = R.mipmap.ic_launcher_foreground
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = activityInjector
}
