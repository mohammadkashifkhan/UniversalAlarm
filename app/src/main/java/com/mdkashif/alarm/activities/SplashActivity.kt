package com.mdkashif.alarm.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.crashlytics.android.Crashlytics
import com.mdkashif.alarm.R
import io.fabric.sdk.android.Fabric

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_splash)

        //        int currentNightMode = getResources().getConfiguration().uiMode
        //                & Configuration.UI_MODE_NIGHT_MASK;
        //        switch (currentNightMode) {
        //            case Configuration.UI_MODE_NIGHT_NO:
        //                showToast("light");
        //            case Configuration.UI_MODE_NIGHT_YES:
        //                showToast("dark");
        //            case Configuration.UI_MODE_NIGHT_UNDEFINED:
        //                showToast("und");
        //        }

        val splashTimeOut = applicationContext.resources.getInteger(R.integer.splash_Time_Out)
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, AppIntroActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            finish()
        }, splashTimeOut.toLong())

    }
}
