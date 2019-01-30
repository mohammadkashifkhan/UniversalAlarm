package com.mdkashif.universalarm.alarm.miscellaneous.misc

class AlarmHelper {

    companion object {

        private var alarmHelper: AlarmHelper? = null

        val instance: AlarmHelper
            get() {
                if (alarmHelper == null) {
                    alarmHelper = AlarmHelper()
                }
                return alarmHelper!!
            }
    }

    fun fetchAlarmsFromDB() {
        // TODO: fetch from Db
        //RoomHelper.transactAsync(AppDatabase.getAppDatabase(activity))
    }

    fun startAlarm() {
//        val dat = Date()//initializes to now
//        val cal_alarm = Calendar.getInstance()
//        val cal_now = Calendar.getInstance()
//        cal_now.time = dat
//        cal_alarm.time = dat
//        cal_alarm.set(Calendar.HOUR_OF_DAY, 2)
//        cal_alarm.set(Calendar.MINUTE, 35)
//        cal_alarm.set(Calendar.SECOND, 0)
//        if (cal_alarm.before(cal_now)) {
//            cal_alarm.add(Calendar.DATE, 1)
//        }
//
//        val intent = Intent(activity,
//                AlarmReceiver::class.java)
//        val pintent = PendingIntent.getBroadcast(activity, 393, intent, 0)
//
//        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        if (Build.VERSION.SDK_INT >= 23) {
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal_alarm.timeInMillis, pintent)
//        }
//        else if (Build.VERSION.SDK_INT >= 21) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal_alarm.timeInMillis, pintent)
//        }

        // TODO: send an alert Notification
    }
}
