package com.mdkashif.universalarm.utils

import android.app.Application

import com.facebook.stetho.Stetho
import com.github.omadahealth.lollipin.lib.managers.LockManager
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.security.AntiTheftUnlockActivity

class AlarmApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO: remove at production!
        Stetho.initializeWithDefaults(this)
        val lockManager = LockManager.getInstance()
        lockManager.enableAppLock(this, AntiTheftUnlockActivity::class.java)
        lockManager.appLock.logoId = R.mipmap.ic_launcher_foreground
    }
}
