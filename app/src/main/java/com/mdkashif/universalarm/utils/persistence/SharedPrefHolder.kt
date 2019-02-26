package com.mdkashif.universalarm.utils.persistence

import android.content.Context
import android.content.SharedPreferences
import com.mdkashif.universalarm.utils.AppConstants

/**
 * Created by Kashif on 16-Apr-18.
 */
class SharedPrefHolder(context: Context) {
    private val pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    companion object {
        private const val PREF_FILE = "universal-alarm-sp"

        private var sharedPrefHolder: SharedPrefHolder? = null

        fun getInstance(context: Context): SharedPrefHolder {
            if (sharedPrefHolder == null) {
                sharedPrefHolder = SharedPrefHolder(context)
            }
            return sharedPrefHolder!!
        }
    }

    init {
        pref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    var isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(AppConstants.isFirstTimeLaunch, true)
        set(isFirstTime) {
            editor.putBoolean(AppConstants.isFirstTimeLaunch, isFirstTime)
            editor.commit()
        }

    var ringtoneUri: String?
        get() = pref.getString(AppConstants.ringtoneUri, "")
        set(uri) {
            editor.putString(AppConstants.ringtoneUri, uri.toString())
            editor.commit()
        }

    var vibrateStatus: Boolean
        get() = pref.getBoolean(AppConstants.vibrateStatus, true)
        set(status) {
            editor.putBoolean(AppConstants.vibrateStatus, status)
            editor.commit()
        }

    var city: String?
        get() = pref.getString(AppConstants.themeSelected, "")
        set(city) {
            editor.putString(AppConstants.city, city)
            editor.commit()
        }

    var country: String?
        get() = pref.getString(AppConstants.themeSelected, "")
        set(country) {
            editor.putString(AppConstants.country, country)
            editor.commit()
        }

    var hbl: Float?
        get() = pref.getFloat(AppConstants.highBatteryLevel, 0f)
        set(level) {
            editor.putFloat(AppConstants.highBatteryLevel, level!!)
            editor.commit()
        }

    var lbl: Float?
        get() = pref.getFloat(AppConstants.lowBatteryLevel, 0f)
        set(level) {
            editor.putFloat(AppConstants.lowBatteryLevel, level!!)
            editor.commit()
        }

    var temp: Float?
        get() = pref.getFloat(AppConstants.batteryTempLevel, 0f)
        set(level) {
            editor.putFloat(AppConstants.batteryTempLevel, level!!)
            editor.commit()
        }

    var batteryAlarmStatus: Boolean?
        get() = pref.getBoolean(AppConstants.batteryAlarmStatus, false)
        set(status) {
            editor.putBoolean(AppConstants.batteryAlarmStatus, status!!)
            editor.commit()
        }

    var temperatureAlarmStatus: Boolean?
        get() = pref.getBoolean(AppConstants.temperatureAlarmStatus, false)
        set(status) {
            editor.putBoolean(AppConstants.temperatureAlarmStatus, status!!)
            editor.commit()
        }

    var theftAlarmStatus: Boolean?
        get() = pref.getBoolean(AppConstants.theftAlarmStatus, false)
        set(status) {
            editor.putBoolean(AppConstants.theftAlarmStatus, status!!)
            editor.commit()
        }

    var theme: String?
        get() = pref.getString(AppConstants.themeSelected, AppConstants.themeLight)
        set(theme) {
            editor.putString(AppConstants.themeSelected, theme)
            editor.commit()
        }

    var snoozeTimeArrayPosition: Int
        get() = pref.getInt(AppConstants.snoozeTimeArrayPosition, 0)
        set(position) {
            editor.putInt(AppConstants.snoozeTimeArrayPosition, 0)
            editor.commit()
        }

    fun clearData() {
        editor.clear().commit()
    }
}
