package com.mdkashif.universalarm.alarm.miscellaneous

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.miscellaneous.model.TimingsModel
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder

class AlarmsListAdapter(private val alarms: MutableList<TimingsModel>, private val viewType: String, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val alarmsList: MutableList<TimingsModel>
        get() = alarms

    internal inner class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTime: TextView = itemView.findViewById(R.id.tvTime)
        var tvTimeType: TextView = itemView.findViewById(R.id.tvTimeType)
        var tvRepeatDays: TextView = itemView.findViewById(R.id.tvRepeatDays)
        var tvETA: TextView = itemView.findViewById(R.id.tvETA)
        var swTime: CompoundButton = itemView.findViewById(R.id.swTime)
    }

    internal inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        var tvCity: TextView = itemView.findViewById(R.id.tvCity)
        var tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        var swLocation: CompoundButton = itemView.findViewById(R.id.swLocation)
    }

    internal inner class BatteryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvHbl: TextView = itemView.findViewById(R.id.tvHbl)
        var tvLbl: TextView = itemView.findViewById(R.id.tvLbl)
        var tvTemp: TextView = itemView.findViewById(R.id.tvTemp)
        var swBattery: CompoundButton = itemView.findViewById(R.id.swBattery)
    }

    internal inner class PrayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvPrayerType: TextView = itemView.findViewById(R.id.tvPrayerType)
        var tvPrayerTime: TextView = itemView.findViewById(R.id.tvPrayerTime)
        var tvPrayerTimeType: TextView = itemView.findViewById(R.id.tvPrayerTimeType)
        var tvETA: TextView = itemView.findViewById(R.id.tvETA)
        var swPrayer: CompoundButton = itemView.findViewById(R.id.swPrayer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        lateinit var viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            0 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_time_list, parent, false)
                viewHolder = TimeViewHolder(view)
            }
            1 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_battery_list, parent, false)
                viewHolder = BatteryViewHolder(view)
            }
            2 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_location_list, parent, false)
                viewHolder = LocationViewHolder(view)
            }
            3 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_prayer_list, parent, false)
                viewHolder = PrayerViewHolder(view)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlarmsListAdapter.TimeViewHolder -> {
                var builder= StringBuilder()
                when {
                    alarmsList[position].hour.toInt() > 12 -> {
                        holder.tvTime.text = "0${(alarmsList[position].hour.toInt() - 12)}:${alarmsList[position].minute}"
                        holder.tvTimeType.text = "PM"
                    }
                    else -> {
                        holder.tvTime.text = """${alarmsList[position].hour}:${alarmsList[position].minute}"""
                        holder.tvTimeType.text = "AM"
                    }
                }
                for (i in alarmsList[position].repeatDays!!.indices) {
                    builder.append(alarmsList[position].repeatDays!![i].repeatDay.substring(0, 1)).append(" ")
                }
                holder.tvRepeatDays.text = builder
                holder.tvETA.text = "ETA: 6 hrs to go"
                holder.swTime.isChecked = alarmsList[position].status
            }
            is AlarmsListAdapter.BatteryViewHolder -> {
                holder.tvHbl.text = SharedPrefHolder.getInstance(context).hbl.toString()
                holder.tvLbl.text = SharedPrefHolder.getInstance(context).lbl.toString()
                holder.tvTemp.text = SharedPrefHolder.getInstance(context).temp.toString()
                holder.swBattery.isChecked = SharedPrefHolder.getInstance(context).batteryAlarmStatus!!
            }
            is AlarmsListAdapter.LocationViewHolder -> {
                holder.tvAddress.text = "Jumeirah Beach"
                holder.tvCity.text = "Dubai"
                holder.tvDistance.text = "3km away"
                holder.swLocation.isChecked = false
            }
            is AlarmsListAdapter.PrayerViewHolder -> {
                holder.tvPrayerType.text = alarmsList[position].alarmType
                when {
                    alarmsList[position].hour.toInt() > 12 -> {
                        holder.tvPrayerTime.text = "0${(alarmsList[position].hour.toInt() - 12)}:${alarmsList[position].minute}"
                        holder.tvPrayerTimeType.text = "PM"
                    }
                    else -> {
                        holder.tvPrayerTime.text = """${alarmsList[position].hour}:${alarmsList[position].minute}"""
                        holder.tvPrayerTimeType.text = "AM"
                    }
                }
                holder.tvETA.text = "ETA: 2 hrs to go"
                holder.swPrayer.isChecked = alarmsList[position].status
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
//        if (AlarmTypes.Location.toString() == alarmsList[position].alarmType)
//            return 2
        return when {
            position<alarmsList.size -> if (AlarmTypes.Time.toString() == alarmsList[position].alarmType) 0
            else 3
            position - alarmsList.size < 2 -> 1
            else -> -1
        }
    }

    override fun getItemCount(): Int {
        if (viewType == "ShowAll" && SharedPrefHolder.getInstance(context).hbl != 0f)
            return alarmsList.size + 1
        else if (viewType == "Home")
            return 4
        return 0
    }

    fun removeItem(position: Int) {
        alarmsList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: TimingsModel, position: Int) {
        alarmsList.add(position, item)
        notifyItemInserted(position)
    }
}