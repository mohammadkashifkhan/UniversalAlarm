package com.mdkashif.alarm.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.mdkashif.alarm.R
import com.mdkashif.alarm.utils.SharedPrefHolder
import kotlinx.android.synthetic.main.activity_app_intro.*

class AppIntroActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!SharedPrefHolder.getInstance(this).isFirstTimeLaunch) {
            goToHomeFragment()
            finish()
        }

        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        setContentView(R.layout.activity_app_intro)

        changeStatusBarColor()

        btGotIt!!.setOnClickListener { goToHomeFragment() }
    }

    private fun goToHomeFragment() {
        SharedPrefHolder.getInstance(this).isFirstTimeLaunch = false

        startActivity(Intent(this@AppIntroActivity, ContainerActivity::class.java))

        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        finish()
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

}

