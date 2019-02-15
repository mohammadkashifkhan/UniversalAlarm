package com.mdkashif.universalarm

import android.app.Application
import com.facebook.stetho.Stetho
import com.github.omadahealth.lollipin.lib.managers.LockManager
import com.mdkashif.universalarm.security.AntiTheftUnlockActivity
import com.mdkashif.universalarm.utils.di.ActivityComponent
import com.mdkashif.universalarm.utils.di.ActivityModule
import com.mdkashif.universalarm.utils.di.DaggerActivityComponent

class AlarmApplication : Application() {
    private lateinit var activityComponent: ActivityComponent

    override fun onCreate() {
        super.onCreate()
        activityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .build()

        // TODO: remove at production!
        Stetho.initializeWithDefaults(this)
        val lockManager = LockManager.getInstance()
        lockManager.enableAppLock(this, AntiTheftUnlockActivity::class.java)
        lockManager.appLock.logoId = R.mipmap.ic_launcher_foreground
    }

    fun getActivityComponent() = activityComponent

}
