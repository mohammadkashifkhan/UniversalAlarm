package com.mdkashif.universalarm

import android.app.Activity
import android.app.Application
import com.facebook.stetho.Stetho
import com.github.omadahealth.lollipin.lib.managers.LockManager
import com.mdkashif.universalarm.security.AntiTheftUnlockActivity
import com.mdkashif.universalarm.utils.di.*
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BaseApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .contextModule(ContextModule(this))
                .roomModule(RoomModule())
                .retrofitModule(RetrofitModule())
                .sharedPrefsModule(SharedPrefsModule())
                .build().inject(this)

        // TODO: remove at production!
        Stetho.initializeWithDefaults(this)
        val lockManager = LockManager.getInstance()
        lockManager.enableAppLock(this, AntiTheftUnlockActivity::class.java)
        lockManager.appLock.logoId = R.mipmap.ic_launcher_foreground
    }

    override fun activityInjector(): AndroidInjector<Activity>  = activityInjector
}
