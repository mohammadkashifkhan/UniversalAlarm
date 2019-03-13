package com.mdkashif.universalarm.persistence

import android.content.Context
import android.content.SharedPreferences
import com.mdkashif.universalarm.utils.AppConstants

/**
 * Created by Kashif on 16-Apr-18.
 */
object AppPreferences {
    private const val prefFileName = "universal-alarm-sp"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isFirstTimeLaunch: Boolean
        get() = preferences.getBoolean(AppConstants.isFirstTimeLaunch, true)
        set(isFirstTime) =
            preferences.edit { it.putBoolean(AppConstants.isFirstTimeLaunch, isFirstTime) }


    var ringtoneUri: String?
        get() = preferences.getString(AppConstants.ringtoneUri, "")
        set(uri) =
            preferences.edit {
                it.putString(AppConstants.ringtoneUri, uri.toString())
            }

    var vibrateStatus: Boolean
        get() = preferences.getBoolean(AppConstants.vibrateStatus, true)
        set(status) =
            preferences.edit {
                it.putBoolean(AppConstants.vibrateStatus, status)
            }

    var city: String?
        get() = preferences.getString(AppConstants.city, "")
        set(city) =
            preferences.edit {
                it.putString(AppConstants.city, city)
            }

    var country: String?
        get() = preferences.getString(AppConstants.country, "")
        set(country) =
            preferences.edit {
                it.putString(AppConstants.country, country)
            }

    var hbl: Float?
        get() = preferences.getFloat(AppConstants.highBatteryLevel, 0f)
        set(level) =
            preferences.edit {
                it.putFloat(AppConstants.highBatteryLevel, level!!)
            }

    var lbl: Float?
        get() = preferences.getFloat(AppConstants.lowBatteryLevel, 0f)
        set(level) =
            preferences.edit {
                it.putFloat(AppConstants.lowBatteryLevel, level!!)
            }

    var temp: Float?
        get() = preferences.getFloat(AppConstants.batteryTempLevel, 0f)
        set(level) =
            preferences.edit {
                it.putFloat(AppConstants.batteryTempLevel, level!!)
            }

    var batteryAlarmStatus: Boolean?
        get() = preferences.getBoolean(AppConstants.batteryAlarmStatus, false)
        set(status) =
            preferences.edit {
                it.putBoolean(AppConstants.batteryAlarmStatus, status!!)
            }

    var temperatureAlarmStatus: Boolean?
        get() = preferences.getBoolean(AppConstants.temperatureAlarmStatus, false)
        set(status) =
            preferences.edit {
                it.putBoolean(AppConstants.temperatureAlarmStatus, status!!)
            }

    var theftAlarmStatus: Boolean?
        get() = preferences.getBoolean(AppConstants.theftAlarmStatus, false)
        set(status) =
            preferences.edit {
                it.putBoolean(AppConstants.theftAlarmStatus, status!!)
            }

    var snoozeTimeArrayPosition: Int
        get() = preferences.getInt(AppConstants.snoozeTimeArrayPosition, 0)
        set(position) =
            preferences.edit {
                it.putInt(AppConstants.snoozeTimeArrayPosition, position)
            }

    var locationPrecisionArrayPosition: Int
        get() = preferences.getInt(AppConstants.locationPrecisionArrayPosition, 0)
        set(position) =
            preferences.edit {
                it.putInt(AppConstants.locationPrecisionArrayPosition, position)
            }

    var timezone: String?
        get() = preferences.getString(AppConstants.timezone, "")
        set(timezone) =
            preferences.edit {
                it.putString(AppConstants.timezone, timezone)
            }

    var islamicDate: String?
        get() = preferences.getString(AppConstants.islamicDate, "")
        set(date) =
            preferences.edit {
                it.putString(AppConstants.islamicDate, date)
            }

    var islamicMonth: String?
        get() = preferences.getString(AppConstants.islamicMonth, "")
        set(month) =
            preferences.edit {
                it.putString(AppConstants.islamicMonth, month)
            }
}
