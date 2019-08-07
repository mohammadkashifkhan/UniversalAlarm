package com.mdkashif.universalarm.misc.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.crashlytics.android.Crashlytics
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.base.BaseActivity
import com.mdkashif.universalarm.persistence.AppPreferences
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity() {
    private val appPreferences: AppPreferences by inject()

    private lateinit var mIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_splash)
//        delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        var wiggle: Animation = AnimationUtils.loadAnimation(this, R.anim.splash_wiggle)
        ivSplash.startAnimation(wiggle)

        val splashTimeOut = applicationContext.resources.getInteger(R.integer.splash_Time_Out)
        Handler().postDelayed({
            mIntent = if (!appPreferences.isFirstTimeLaunch)
                Intent(this@SplashActivity, ContainerActivity::class.java)
            else
                Intent(this@SplashActivity, AppIntroActivity::class.java)
            executeIntent(mIntent, true)
        }, splashTimeOut.toLong())

    }
}
