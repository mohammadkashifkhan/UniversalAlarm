package com.mdkashif.universalarm.utils

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.core.app.TaskStackBuilder
import com.mdkashif.universalarm.activities.BaseActivity
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder

class ThemeHelper {
    companion object {
        // Use this to set theme through settings
        fun switchTheme(context: Context, mActivity: BaseActivity, theme: String): Boolean {
            SharedPrefHolder.getInstance(mActivity).theme = theme
            when (theme) {
                "Auto" -> {
                    detectThemeAuto(mActivity)
                    setTheme()
                }
                "Light" -> {
                    setTheme()
                }
                "Dark" -> {
                    setTheme()
                }
            }
        }

        private fun detectThemeAuto(mActivity: BaseActivity): String {
            lateinit var autoTheme: String
            when (mActivity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> autoTheme = AppConstants.themeLight

                Configuration.UI_MODE_NIGHT_YES -> autoTheme = AppConstants.themeDark

                Configuration.UI_MODE_NIGHT_UNDEFINED -> autoTheme = "undefined"
            }
            return autoTheme
        }

        // Use this to set it when activity and Fragment starts
        fun setTheme(context: Context, mActivity: BaseActivity, theme: String): Boolean {
            val intent = mActivity.intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            mActivity.finish()
            mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            mActivity.startActivity(intent)

            TaskStackBuilder.create(mActivity.applicationContext)
                    .addNextIntent(Intent(mActivity, BaseActivity::class.java))
                    .addNextIntent(mActivity.intent)
                    .startActivities()

            context.setTheme(ThemeHelper.active(context))
        }
    }
}