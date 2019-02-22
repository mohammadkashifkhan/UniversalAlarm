package com.mdkashif.universalarm.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.crashlytics.android.Crashlytics
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {
    private lateinit var mIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_splash)

        var wiggle: Animation = AnimationUtils.loadAnimation(this, R.anim.splash_wiggle)
        ivSplash.startAnimation(wiggle)

        val splashTimeOut = applicationContext.resources.getInteger(R.integer.splash_Time_Out)
        Handler().postDelayed({
            mIntent = if (!SharedPrefHolder.getInstance(this).isFirstTimeLaunch)
                Intent(this@SplashActivity, ContainerActivity::class.java)
            else
                Intent(this@SplashActivity, AppIntroActivity::class.java)
            executeIntent(mIntent, true)
        }, splashTimeOut.toLong())

    }
}
