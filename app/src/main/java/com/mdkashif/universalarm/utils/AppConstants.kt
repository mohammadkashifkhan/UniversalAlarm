package com.mdkashif.universalarm.utils

/**
 * Created by Kashif on 16-Apr-18.
 */
object AppConstants {

    /////// Endpoints ///////
    private const val MAIN_URL = "http://www.mdkashif.com/UniversalAlarm/"
    const val GET_PRAYER_DATA = "http://api.aladhan.com/v1/"
    const val FAQ = MAIN_URL + "faq.html"
    const val PP = MAIN_URL + "privacy-policy.html"
    const val TNC = MAIN_URL + "terms-and-conditions.html"

    /////// Miscellaneous ///////
    const val isFirstTimeLaunch = "isFirstTimeLaunch"
    const val ringtoneUri = "ringtone_uri"
    const val vibrateStatus = "vibrate_status"

    const val themeLight = "Light"
    const val themeDark = "Dark"

    const val city = "city"
    const val country = "country"

    const val snoozeTimeArrayPosition = "snoozeTimeArrayPosition"
    const val timezone = "timezone"
    const val islamicDate = "islamicDate"
    const val islamicMonth = "islamicMonth"

    /////// SetBatteryLevelFragment ///////
    const val highBatteryLevel = "hbl"
    const val lowBatteryLevel = "lbl"
    const val batteryTempLevel = "temp"
    const val batteryAlarmStatus = "batteryAlarmStatus"
    const val temperatureAlarmStatus = "temperatureAlarmStatus"
    const val theftAlarmStatus = "theftAlarmStatus"

    /////// Notification ///////
    const val notificationId = 786
    const val notificationChannelName = "Miscellaneous"
    const val notificationChannelId = "com.mdkashif.alarm.notifs"
    const val notificationDescription = "Alarms"
}
