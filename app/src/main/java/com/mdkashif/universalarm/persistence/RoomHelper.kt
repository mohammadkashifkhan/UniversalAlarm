package com.mdkashif.universalarm.persistence


import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.mdkashif.universalarm.alarm.misc.AlarmOps
import com.mdkashif.universalarm.alarm.misc.AlarmTypes
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel


class RoomHelper { //@Inject constructor(accessDao: RoomAccessDao)

    companion object {

        fun transactFetchAsync(db: AppDatabase, type: AlarmTypes): Pair<MutableList<TimingsModel>,LiveData<Int>> {
            return when (type) {
                AlarmTypes.Prayer -> TransactDbAsync(db, null, 0).execute(AlarmOps.Get.toString()).get() // instead of specifying each type explicitly, specified prayer to get all of them
                else -> TransactDbAsync(db, null, 1).execute(AlarmOps.Get.toString()).get() // passed 1 just to differentiate between prayer and all alarms
            }
        }

        fun transactAmendAsync(db: AppDatabase, taskType: String, timingsModel: TimingsModel, id: Long = 0, autoUpdate: Boolean=false) { //id=0 means we are just inserting
            if (timingsModel.alarmType != AlarmTypes.Time.toString())
                TransactDbAsync(db, timingsModel, id, autoUpdate).execute(AlarmOps.Check.toString()) // to check if we have any existing prayer alarm
            else
                TransactDbAsync(db, timingsModel, id).execute(taskType)
        }

        private fun addTimingsWithoutRepeatDays(db: AppDatabase, timingsModel: TimingsModel): Long {
            return db.accessDao().addNewTimeAlarm(timingsModel)
        }

        private fun addTimingsWithRepeatDays(db: AppDatabase, timingsModel: TimingsModel) {
            val alarmId = db.accessDao().addNewTimeAlarm(timingsModel)
            for (i in timingsModel.repeatDays!!.indices) {
                timingsModel.repeatDays!![i].fkAlarmId = alarmId
                db.accessDao().addRepeatDays(timingsModel.repeatDays!![i])
            }
        }

        private fun getSpecificTimings(db: AppDatabase, timingsModel: TimingsModel): MutableList<TimingsModel> {
            return db.accessDao().getSpecificAlarms(timingsModel.alarmType)
        }

        private fun getPrayerTimings(db: AppDatabase): MutableList<TimingsModel> {
            return db.accessDao().getPrayerAlarms()
        }

        private fun getTimingsWithDays(db: AppDatabase): MutableList<TimingsModel> {
            val timings = db.accessDao().getAllAlarms()
            for (i in timings.indices) {
                if (db.accessDao().getRepeatDays(timings[i].id).isNotEmpty()) {
                    timings[i].repeatDays = db.accessDao().getRepeatDays(timings[i].id)
                }
            }
            return timings
        }

        private fun deleteAlarm(db: AppDatabase, id: Long) {
            db.accessDao().deleteAlarm(id)
        }

        private fun updateAlarm(db: AppDatabase, timingsModel: TimingsModel, id: Long, autoUpdate: Boolean) {
            try {
                db.accessDao().updateAlarm(timingsModel.hour, timingsModel.minute, timingsModel.note, timingsModel.repeat, timingsModel.status, id, autoUpdate)
            }catch (e: Throwable) { println(e.message) }
            if (timingsModel.repeat) {
                for (i in timingsModel.repeatDays!!.indices) {
                    timingsModel.repeatDays!![i].fkAlarmId = id
                    db.accessDao().addRepeatDays(timingsModel.repeatDays!![i])
                }
            }
        }

        private fun countAllAlarms(db: AppDatabase): LiveData<Int> {
            return db.accessDao().countAlarms()
        }

        private class TransactDbAsync(private val db: AppDatabase, private val timingsModel: TimingsModel?, private val alarmId: Long, private val autoUpdate: Boolean= false) : AsyncTask<String, Any, Pair<MutableList<TimingsModel>,LiveData<Int>>>() {
            var timingsList: MutableList<TimingsModel> = ArrayList()

            override fun onPreExecute() {
                super.onPreExecute()
                timingsList.clear()
            }
            override fun doInBackground(vararg params: String): Pair<MutableList<TimingsModel>,LiveData<Int>>? {
                when (params[0]) {
                    AlarmOps.Get.toString() -> {
                        (return if (alarmId == 0.toLong())
                            Pair(getPrayerTimings(db), countAllAlarms(db))
                        else
                            Pair(getTimingsWithDays(db), countAllAlarms(db))) //TODO : combine location alarms here as well
                    }
                    AlarmOps.Check.toString() -> {
                        timingsList = getSpecificTimings(db, timingsModel!!)

                        if (timingsList.isNotEmpty())
                            TransactDbAsync(db, timingsModel, timingsList[0].id, autoUpdate).execute(AlarmOps.Update.toString()) // update an existing prayer alarm
                        else
                            TransactDbAsync(db, timingsModel, alarmId).execute(AlarmOps.Add.toString()) // add the prayer alarm
                    }
                    AlarmOps.Add.toString() -> {
                        if (timingsModel!!.repeat) {
                            addTimingsWithRepeatDays(db, timingsModel)
                        } else {
                            addTimingsWithoutRepeatDays(db, timingsModel)
                        }
                    }
                    AlarmOps.Update.toString() -> {
                        if (alarmId > 0)
                            updateAlarm(db, timingsModel!!, alarmId, autoUpdate)
                    }
                    AlarmOps.Delete.toString() -> {
                        if (alarmId > 0)
                            deleteAlarm(db, alarmId)
                    }
                }
                return null
            }
        }
    }
}