package com.mdkashif.universalarm.alarm.misc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hendraanggrian.recyclerview.widget.ExpandableRecyclerView
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import com.mdkashif.universalarm.alarm.time.TimePresenter
import com.mdkashif.universalarm.persistence.AppPreferences

class AlarmsListAdapter(private val alarmsList: MutableList<TimingsModel>, private val viewType: String, private val context: Context, linearLayoutManager: LinearLayoutManager) : ExpandableRecyclerView.Adapter<RecyclerView.ViewHolder>(linearLayoutManager) {

    internal inner class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvTimeType: TextView = itemView.findViewById(R.id.tvTimeType)
        val tvRepeatDays: TextView = itemView.findViewById(R.id.tvRepeatDays)
        val tvETA: TextView = itemView.findViewById(R.id.tvETA)
        val swTime: SwitchCompat = itemView.findViewById(R.id.swTime)
    }

    internal inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvCity: TextView = itemView.findViewById(R.id.tvCity)
        val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        val swLocation: SwitchCompat = itemView.findViewById(R.id.swLocation)
    }

    internal inner class BatteryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHbl: TextView = itemView.findViewById(R.id.tvHbl)
        val tvLbl: TextView = itemView.findViewById(R.id.tvLbl)
        val tvTemp: TextView = itemView.findViewById(R.id.tvTemp)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
        val swBattery: SwitchCompat = itemView.findViewById(R.id.swBattery)
        val swTemperature: SwitchCompat = itemView.findViewById(R.id.swTemperature)
    }

    internal inner class PrayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPrayerType: TextView = itemView.findViewById(R.id.tvPrayerType)
        val tvPrayerTime: TextView = itemView.findViewById(R.id.tvPrayerTime)
        val tvPrayerTimeType: TextView = itemView.findViewById(R.id.tvPrayerTimeType)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
        val tvETA: TextView = itemView.findViewById(R.id.tvETA)
        val swPrayer: SwitchCompat = itemView.findViewById(R.id.swPrayer)
    }

    internal inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPlaceHolder: ImageView = itemView.findViewById(R.id.ivPlaceHolder)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        lateinit var viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            0 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_expandable_time, parent, false)
                viewHolder = TimeViewHolder(view)
            }
            1 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_expandable_battery, parent, false)
                viewHolder = BatteryViewHolder(view)
            }
            2 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_expandable_location, parent, false)
                viewHolder = LocationViewHolder(view)
            }
            3 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_expandable_prayer, parent, false)
                viewHolder = PrayerViewHolder(view)
            }
            -1 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_expandable_empty, parent, false)
                viewHolder = EmptyViewHolder(view)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (holder) {
            is AlarmsListAdapter.TimeViewHolder -> {
                var builder = StringBuilder()
                when {
                    alarmsList[position].hour.toInt() >= 12 -> {
                        val hour = alarmsList[position].hour.toInt()
                        when {
                            hour == 12 -> holder.tvTime.text = "$hour:${alarmsList[position].minute}"
                            hour - 12 >= 10 -> holder.tvTime.text = "${hour - 12}:${alarmsList[position].minute}"
                            else -> holder.tvTime.text = "0${hour - 12}:${alarmsList[position].minute}"
                        }
                        holder.tvTimeType.text = "PM"
                    }
                    else -> {
                        val hour = alarmsList[position].hour.toInt()
                        if (hour >= 10)
                            holder.tvTime.text = "$hour:${alarmsList[position].minute}"
                        else
                            holder.tvTime.text = "0$hour:${alarmsList[position].minute}"
                        holder.tvTimeType.text = "AM"
                    }
                }
                if (alarmsList[position].repeat) {
                    for (i in alarmsList[position].repeatDays!!.indices) {
                        builder.append(alarmsList[position].repeatDays!![i].repeatDay.substring(0, 1)).append(" ")
                    }
                }
                holder.tvRepeatDays.text = builder
                holder.tvETA.text = "ETA: ${TimePresenter.calculateTimeFromNow(alarmsList[position].hour.toInt(), alarmsList[position].minute.toInt())} from now"
                holder.swTime.isChecked = alarmsList[position].status
                holder.swTime.setOnCheckedChangeListener { p0, p1 ->
//                    AppPreferences.batteryAlarmStatus = p1
                }
            }
            is AlarmsListAdapter.BatteryViewHolder -> {
                holder.tvHbl.text = AppPreferences.hbl.toString()
                holder.tvLbl.text = AppPreferences.lbl.toString()
                holder.tvTemp.text = AppPreferences.temp.toString()
                holder.tvNote.text = context.getText(R.string.tvItemBatteryExpandableRv)
                holder.swBattery.isChecked = AppPreferences.batteryAlarmStatus!!
                holder.swTemperature.isChecked = AppPreferences.temperatureAlarmStatus!!
                holder.swBattery.setOnCheckedChangeListener { p0, p1 ->
                    AppPreferences.batteryAlarmStatus = p1 }
                holder.swTemperature.setOnCheckedChangeListener { p0, p1 ->
                    AppPreferences.temperatureAlarmStatus = p1 }
            }
            is AlarmsListAdapter.LocationViewHolder -> {
                holder.tvAddress.text = "Jumeirah Beach"
                holder.tvCity.text = "Dubai"
                holder.tvDistance.text = "3km away"
                holder.swLocation.isChecked = false
            }
            is AlarmsListAdapter.PrayerViewHolder -> {
                holder.tvPrayerType.text = alarmsList[position].alarmType
                holder.tvNote.text = context.getText(R.string.tvItemPrayerExpandableRv)
                when {
                    alarmsList[position].hour.toInt() >= 12 -> {
                        val hour = alarmsList[position].hour.toInt()
                        when {
                            hour == 12 -> holder.tvPrayerTime.text = "$hour:${alarmsList[position].minute}"
                            hour - 12 >= 10 -> holder.tvPrayerTime.text = "${hour - 12}:${alarmsList[position].minute}"
                            else -> holder.tvPrayerTime.text = "0${hour - 12}:${alarmsList[position].minute}"
                        }
                        holder.tvPrayerTimeType.text = "PM"
                    }
                    else -> {
                        holder.tvPrayerTime.text = """${alarmsList[position].hour}:${alarmsList[position].minute}"""
                        holder.tvPrayerTimeType.text = "AM"
                    }
                }
                holder.tvETA.text = "ETA: ${TimePresenter.calculateTimeFromNow(alarmsList[position].hour.toInt(), alarmsList[position].minute.toInt())} from now"
                holder.swPrayer.isChecked = alarmsList[position].status
                holder.swPrayer.setOnCheckedChangeListener { p0, p1 ->
                    //                    AppPreferences.batteryAlarmStatus = p1
                }
            }
            is AlarmsListAdapter.EmptyViewHolder -> {
                holder.tvNote.text = context.getText(R.string.tvItemEmptyExpandableRv)
                Glide.with(context)
                        .load(R.raw.no_alarms_placeholder)
                        .into(holder.ivPlaceHolder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
//        if (AlarmTypes.Location.toString() == alarmsList[position].alarmType)
//            return 2
        return when {
            position < alarmsList.size -> if (AlarmTypes.Time.toString() == alarmsList[position].alarmType) 0
            else 3
            position - alarmsList.size < 2 && AppPreferences.hbl != 0f -> 1
            else -> -1
        }
    }

    override fun getItemCount(): Int {
        when (viewType) {
            "ShowAll" -> return if (AppPreferences.hbl != 0f)
                alarmsList.size + 1
            else
                alarmsList.size
            "Home" -> when {
                alarmsList.size > 4 -> return 4
                alarmsList.size in 1..4 -> return if (AppPreferences.hbl != 0f)
                    alarmsList.size + 1
                else
                    alarmsList.size
                AppPreferences.hbl != 0f -> return 1
            }
        }
        return 1
    }

    fun removeItem(position: Int) {
        alarmsList.removeAt(position)
        notifyItemRemoved(position)
    }
}