package com.mdkashif.universalarm.utils.persistence


import android.os.AsyncTask
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmOps
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmTypes
import com.mdkashif.universalarm.alarm.miscellaneous.model.TimingsModel


class RoomHelper { //@Inject constructor(accessDao: RoomAccessDao)

    companion object {

        fun transactFetchAsync(db: AppDatabase, type: AlarmTypes): Pair<List<TimingsModel>,Int> {
            return if (type == AlarmTypes.Prayer) // instead of specifying each type explicitely, specified prayer to get all of them
                TransactDbAsync(db, null, 0).execute(AlarmOps.Get.toString()).get()
            else
                TransactDbAsync(db, null, 1).execute(AlarmOps.Get.toString()).get() // passed 1 just to differentiate between time and prayer alarm
        }

        fun transactAmendAsync(db: AppDatabase, taskType: String, timingsModel: TimingsModel, id: Long = 0) { //id=0 means we are just inserting
            if (timingsModel.alarmType != AlarmTypes.Time.toString())
                TransactDbAsync(db, timingsModel, id).execute(AlarmOps.Check.toString()) // to check if we have any existing prayer alarm
            else
                TransactDbAsync(db, timingsModel, id).execute(taskType)
        }

        fun transactCountAsync(db: AppDatabase): Pair<List<TimingsModel>,Int> {
            return TransactDbAsync(db, null, 0).execute(AlarmOps.Count.toString()).get()
        }

        private fun addTimingsWithoutRepeatDays(db: AppDatabase, timingsModel: TimingsModel): Long {
            return db.accessDao().addNewAlarm(timingsModel)
        }

        private fun addTimingsWithRepeatDays(db: AppDatabase, timingsModel: TimingsModel) {
            val alarmId = db.accessDao().addNewAlarm(timingsModel)
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

        private fun updateAlarm(db: AppDatabase, timingsModel: TimingsModel, id: Long) {
            db.accessDao().updateAlarm(timingsModel.hour, timingsModel.minute, timingsModel.note, timingsModel.repeat, timingsModel.status, id)
            if (timingsModel.repeat) {
                for (i in timingsModel.repeatDays!!.indices) {
                    timingsModel.repeatDays!![i].fkAlarmId = id
                    db.accessDao().addRepeatDays(timingsModel.repeatDays!![i])
                }
            }
        }

        private fun countAllAlarms(db: AppDatabase): Int {
            return db.accessDao().countAlarms()
        }

        private class TransactDbAsync(private val db: AppDatabase, private val timingsModel: TimingsModel?, private val alarmId: Long) : AsyncTask<String, Any, Pair<MutableList<TimingsModel>,Int>>() {
            var timingsList: MutableList<TimingsModel> = ArrayList()

            override fun onPreExecute() {
                super.onPreExecute()
                timingsList.clear()
            }
            override fun doInBackground(vararg params: String): Pair<MutableList<TimingsModel>,Int>? {
                when (params[0]) {
                    AlarmOps.Get.toString() -> {
                        (return if (alarmId == 0.toLong())
                            Pair(getPrayerTimings(db),0)
                        else
                            Pair(getTimingsWithDays(db),0))
                    }
                    AlarmOps.Check.toString() -> {
                        timingsList = getSpecificTimings(db, timingsModel!!)

                        if (timingsList.isNotEmpty())
                            TransactDbAsync(db, timingsModel, timingsList[0].id).execute(AlarmOps.Update.toString()) // update an existing prayer alarm
                        else
                            TransactDbAsync(db, timingsModel, alarmId).execute(AlarmOps.Add.toString()) // add a prayer alarm
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
                            updateAlarm(db, timingsModel!!, alarmId)
                    }
                    AlarmOps.Delete.toString() -> {
                        if (alarmId > 0)
                            deleteAlarm(db, alarmId)
                    }
                    AlarmOps.Count.toString() -> {
                        return Pair(timingsList,countAllAlarms(db))
                    }
                }
                return null
            }
        }
    }
}