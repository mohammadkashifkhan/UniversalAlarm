package com.mdkashif.universalarm.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.base.BaseActivity
import com.mdkashif.universalarm.persistence.AppPreferences
import kotlinx.android.synthetic.main.activity_app_intro.*

class AppIntroActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        setContentView(R.layout.activity_app_intro)

        btGotIt!!.setOnClickListener { AppPreferences().instance.isFirstTimeLaunch = false
            executeIntent(Intent(this@AppIntroActivity, ContainerActivity::class.java), true) }
    }
}

