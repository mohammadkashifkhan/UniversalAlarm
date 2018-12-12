package com.mdkashif.alarm.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.crashlytics.android.Crashlytics
import com.mdkashif.alarm.R
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_splash)

        var wiggle: Animation= AnimationUtils.loadAnimation(this, R.anim.splash_wiggle);
        ivSplash.startAnimation(wiggle)

        val splashTimeOut = applicationContext.resources.getInteger(R.integer.splash_Time_Out)
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, AppIntroActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            finish()
        }, splashTimeOut.toLong())

    }
}
