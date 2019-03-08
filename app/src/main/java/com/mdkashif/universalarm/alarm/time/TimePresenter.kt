package com.mdkashif.universalarm.alarm.time

import java.text.SimpleDateFormat
import java.util.*


object TimePresenter {

    fun calculateTimeFromNow(selectedHour: Int, selectedMinute: Int): String {
        val format = SimpleDateFormat("HH:mm")
        val cal = Calendar.getInstance()

        val selectedTime = "$selectedHour:$selectedMinute"
        val currentTime = format.format(cal.time)

        val date1 = format.parse(currentTime)
        val date2 = format.parse(selectedTime)

        val differenceInSeconds = (date2.time - date1.time) / 1000

        var hours = differenceInSeconds / 3600
        val minutes = differenceInSeconds % 3600 / 60

        if (hours < 0)
            hours += 24
        return """${Math.abs(hours)}h ${Math.abs(minutes)}m"""
    }

    fun getDifferentZonedTimes(id: Int): String {
        val dateFormat = SimpleDateFormat("HH mm")
        when (id) {
            1 -> dateFormat.timeZone = TimeZone.getTimeZone("Asia/Dubai")
            2 -> dateFormat.timeZone = TimeZone.getTimeZone("America/New_York")
            3 -> dateFormat.timeZone = TimeZone.getTimeZone("Australia/Sydney")
            4 -> dateFormat.timeZone = TimeZone.getTimeZone("Europe/Moscow")
            5 -> dateFormat.timeZone = TimeZone.getTimeZone("America/Sao_Paulo")
            6 -> dateFormat.timeZone = TimeZone.getTimeZone("Europe/London")
        }

        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, 0)
        return dateFormat.format(cal.time)
    }

}