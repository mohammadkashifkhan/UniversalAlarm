package com.mdkashif.universalarm.utils

import com.mdkashif.universalarm.BuildConfig

/**
 * Created by Kashif on 16-Apr-18.
 */
object AppConstants {

    /////// Endpoints ///////
    private const val MAIN_URL = "http://www.mdkashif.com/UniversalAlarm/"
    const val GET_PRAYER_DATA_URL = "http://api.aladhan.com/v1/"
    const val FAQ = MAIN_URL + "faq.html"
    const val PP = MAIN_URL + "privacy-policy.html"
    const val TNC = MAIN_URL + "terms-and-conditions.html"

    /////// Miscellaneous ///////
    const val spAlias = "universal-alarm-sp"
    const val dbAlias = "universal-alarm-db"
    const val isFirstTimeLaunch = "isFirstTimeLaunch"
    const val ringtoneUri = "ringtone_uri"
    const val vibrateStatus = "vibrate_status"

    const val playServicesResolutionRequest = 1000
    const val updateInterval = 10000
    const val fastestInterval = 1000
    const val displacement = 1000

    const val city = "city"
    const val country = "country"

    const val snoozeTimeArrayPosition = "snoozeTimeArrayPosition"
    const val locationPrecisionArrayPosition = "locationPrecisionArrayPosition"
    const val timezone = "timezone"
    const val islamicDate = "islamicDate"
    const val islamicMonth = "islamicMonth"
    const val REQUEST_CODE_ENABLE_THEFT_ALARM = 121

    /////// SetBatteryLevelFragment ///////
    const val highBatteryLevel = "hbl"
    const val lowBatteryLevel = "lbl"
    const val batteryTempLevel = "temp"
    const val batteryAlarmStatus = "batteryAlarmStatus"
    const val temperatureAlarmStatus = "temperatureAlarmStatus"
    const val theftAlarmStatus = "theftAlarmStatus"
    const val theftPinEnabled = "theftPinEnabled"

    /////// Notification ///////
    const val notificationId = 786
    const val notificationChannelName = "Miscellaneous"
    const val notificationChannelId = BuildConfig.APPLICATION_ID + ".notifs"
    const val notificationDescription = "Alarms"
}
