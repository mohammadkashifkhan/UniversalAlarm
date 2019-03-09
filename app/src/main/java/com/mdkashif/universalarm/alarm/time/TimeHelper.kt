package com.mdkashif.universalarm.alarm.time

import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object TimeHelper {

    private fun calculateTimeFromNow(selectedHour: Int, selectedMinute: Int): Observable<String> {
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

        return Observable.just("${Math.abs(hours)}h ${Math.abs(minutes)}m")
    }

    fun getTimeFromNow(selectedHour: Int, selectedMinute: Int): Observable<String> {
        return Observable.interval(0, 10, TimeUnit.SECONDS)
                .flatMap<String> { TimeHelper.calculateTimeFromNow(selectedHour, selectedMinute) }
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