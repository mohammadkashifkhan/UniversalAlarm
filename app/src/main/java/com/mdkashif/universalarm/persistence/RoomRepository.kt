package com.mdkashif.universalarm.persistence

import com.mdkashif.universalarm.alarm.misc.AlarmOps
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomRepository @Inject constructor(var dao: RoomAccessDao) {

    suspend fun fetchPrayersAlarmsAsync(): MutableList<TimingsModel> {
        var list: MutableList<TimingsModel> = ArrayList()
        GlobalScope.launch { list = async(Dispatchers.IO) { return@async getPrayerTimings() }.await() }
        return list
    }

    suspend fun fetchTimingsAlarmsAsync(alarmStatus: Boolean = false): MutableList<TimingsModel> {
        var list: MutableList<TimingsModel> = ArrayList()
        GlobalScope.launch { list = async(Dispatchers.IO) { return@async getTimingsWithRepeatDays(alarmStatus) }.await() }
        return list
    }

    fun fetchAllAlarmsAsync(alarmStatus: Boolean = false): Pair<MutableList<TimingsModel>, MutableList<LocationsModel>> {
        var timingslist: MutableList<TimingsModel> = ArrayList()
        var locationslist: MutableList<LocationsModel> = ArrayList()
        GlobalScope.launch(Dispatchers.IO) {
            timingslist = getTimingsWithRepeatDays(alarmStatus)
            locationslist = getLocations(alarmStatus)
        }
        return Pair(timingslist, locationslist)
    }

    suspend fun amendPrayerAlarmsAsync(timingsModel: TimingsModel?, autoUpdate: Boolean = false) { //autoUpdate=false for differentiating between manual & auto update
        var timingsList: MutableList<TimingsModel> = ArrayList()
        GlobalScope.launch { timingsList = async(Dispatchers.IO) { return@async getSpecificTimings(timingsModel!!) }.await() }

        when {
            timingsList.isNotEmpty() -> GlobalScope.launch(Dispatchers.IO) { updateTimeAlarm(timingsModel!!, timingsList[0].id, autoUpdate) }
            else -> GlobalScope.launch(Dispatchers.IO) { addTimingsWithoutRepeatDays(timingsModel!!) }
        }
    }

    fun amendTimingsAlarmsAsync(taskType: String, timingsModel: TimingsModel?, id: Long = 0) { //id=0 means we are just inserting
        when (taskType) {
            AlarmOps.Add.toString() ->
                when {
                    timingsModel!!.repeat -> GlobalScope.launch(Dispatchers.IO) { addTimingsWithRepeatDays(timingsModel) }
                    else -> GlobalScope.launch(Dispatchers.IO) { addTimingsWithoutRepeatDays(timingsModel) }
                }

            AlarmOps.Update.toString() -> GlobalScope.launch(Dispatchers.IO) { updateTimeAlarm(timingsModel!!, id, false) }

            AlarmOps.Delete.toString() -> GlobalScope.launch(Dispatchers.IO) { deleteTimeAlarm(id) }
        }
    }

    fun amendLocationsAlarmsAsync(taskType: String, locationsModel: LocationsModel?, id: Long = 0) {
        when (taskType) {
            AlarmOps.Add.toString() -> GlobalScope.launch(Dispatchers.IO) { addLocation(locationsModel!!) }

            AlarmOps.Update.toString() -> GlobalScope.launch(Dispatchers.IO) { updateLocationAlarm(locationsModel!!, id) }

            AlarmOps.Delete.toString() -> GlobalScope.launch(Dispatchers.IO) { deleteLocationAlarm(id) }
        }
    }

    private fun addTimingsWithoutRepeatDays(timingsModel: TimingsModel): Long {
        return dao.addNewTimeAlarm(timingsModel)
    }

    private fun addTimingsWithRepeatDays(timingsModel: TimingsModel) {
        val alarmId = dao.addNewTimeAlarm(timingsModel)
        for (i in timingsModel.repeatDays!!.indices) {
            timingsModel.repeatDays!![i].fkAlarmId = alarmId
            dao.addRepeatDays(timingsModel.repeatDays!![i])
        }
    }

    private fun addLocation(locationsModel: LocationsModel): Long {
        return dao.addNewLocationAlarm(locationsModel)
    }

    private fun getSpecificTimings(timingsModel: TimingsModel): MutableList<TimingsModel> {
        return dao.getSpecificTimeAlarms(timingsModel.alarmType)
    }

    private fun getPrayerTimings(): MutableList<TimingsModel> {
        return dao.getPrayerAlarms()
    }

    private fun getLocations(status: Boolean = false): MutableList<LocationsModel> {
        return if (status)
            dao.getOnlyLiveLocationAlarms()
        else
            dao.getLocationAlarms()
    }

    private fun getTimingsWithRepeatDays(status: Boolean = false): MutableList<TimingsModel> {
        val timings: MutableList<TimingsModel> = when {
            status -> dao.getOnlyLiveTimeAlarms()
            else -> dao.getAllTimeAlarms()
        }
        for (i in timings.indices) {
            if (dao.getRepeatDays(timings[i].id).isNotEmpty()) {
                timings[i].repeatDays = dao.getRepeatDays(timings[i].id)
            }
        }
        return timings
    }

    private fun deleteTimeAlarm(id: Long) {
        dao.deleteTimeAlarm(id)
    }

    private fun deleteLocationAlarm(id: Long) {
        dao.deleteLocationAlarm(id)
    }

    private fun updateTimeAlarm(timingsModel: TimingsModel, id: Long, autoUpdate: Boolean) {
        dao.updateTimeAlarm(timingsModel.hour, timingsModel.minute, timingsModel.note, timingsModel.repeat, timingsModel.status, id, autoUpdate)
        if (timingsModel.repeat) {
            dao.deleteRepeatDays(timingsModel.id)
            for (i in timingsModel.repeatDays!!.indices) {
                timingsModel.repeatDays!![i].fkAlarmId = id
                dao.addRepeatDays(timingsModel.repeatDays!![i])
            }
        }
    }

    private fun updateLocationAlarm(locationsModel: LocationsModel, id: Long) {
        dao.updateLocationAlarm(locationsModel.address, locationsModel.city, locationsModel.latitude, locationsModel.longitude, locationsModel.status, id)
    }
}
