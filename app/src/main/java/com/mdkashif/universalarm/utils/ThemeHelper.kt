package com.mdkashif.universalarm.utils

import android.content.res.Configuration
import com.mdkashif.universalarm.base.BaseActivity


object ThemeHelper {

    fun detectThemeAuto(mActivity: BaseActivity): String {
        lateinit var autoTheme: String
        when (mActivity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> autoTheme = AppConstants.themeLight

            Configuration.UI_MODE_NIGHT_YES -> autoTheme = AppConstants.themeDark

            Configuration.UI_MODE_NIGHT_UNDEFINED -> autoTheme = "undefined"
        }
        return autoTheme
    }


//    fun setTheme(context: Context) : Context {
//        val res = context.resources
//        var mode = res.configuration.uiMode
//        when (context.theme) {
//            DARK -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                mode = Configuration.UI_MODE_NIGHT_YES
//            }
//            LIGHT -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                mode = Configuration.UI_MODE_NIGHT_NO
//            }
//            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
//        }
//
//        val config = Configuration(res.configuration)
//        config.uiMode = mode
//        context = context.createConfigurationContext(config)
//        return context
//    }

}