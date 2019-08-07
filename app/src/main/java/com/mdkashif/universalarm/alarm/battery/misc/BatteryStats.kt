package com.mdkashif.universalarm.alarm.battery.misc

import android.content.Intent
import android.os.BatteryManager
import java.text.DecimalFormat

class BatteryStats(private val batteryIntent: Intent?) {

    val level: Int
        get() = batteryIntent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)

    val levelAccurate: Float
        get() {
            val level = level
            val scale = scale
            return level / scale.toFloat()
        }

    val scale: Int
        get() = batteryIntent!!.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

    val batteryTechnology: String
        get() = batteryIntent!!.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)

    val pluggedState: Int
        get() {
            return when (batteryIntent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
                BatteryManager.BATTERY_PLUGGED_AC -> PLUGGED_STATE_AC
                BatteryManager.BATTERY_PLUGGED_USB -> PLUGGED_STATE_USB
                BatteryManager.BATTERY_PLUGGED_WIRELESS -> PLUGGED_STATE_WIRELESS
                else -> PLUGGED_STATE_UNKNOWN
            }
        }

    val isCharging: Boolean
        get() {
            val plugState = batteryIntent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)

            return plugState == BatteryManager.BATTERY_PLUGGED_AC ||
                    plugState == BatteryManager.BATTERY_PLUGGED_USB || plugState == BatteryManager.BATTERY_PLUGGED_WIRELESS
        }

    val health: Int
        get() {
            return when (batteryIntent!!.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)) {
                BatteryManager.BATTERY_HEALTH_COLD -> BATTERY_HEALTH_COLD
                BatteryManager.BATTERY_HEALTH_DEAD -> BATTERY_HEALTH_DEAD
                BatteryManager.BATTERY_HEALTH_GOOD -> BATTERY_HEALTH_GOOD
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> BATTERY_HEALTH_OVER_VOLTAGE
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> BATTERY_HEALTH_OVERHEAT
                BatteryManager.BATTERY_HEALTH_UNKNOWN -> BATTERY_HEALTH_UNKNOWN
                else -> BATTERY_STATUS_FAILURE
            }
        }

    val healthText: String
        get() {
            return when (health) {
                BATTERY_HEALTH_COLD -> "Cold"
                BATTERY_HEALTH_DEAD -> "Dead"
                BATTERY_HEALTH_GOOD -> "Good"
                BATTERY_HEALTH_OVER_VOLTAGE -> "Bad"
                BATTERY_HEALTH_OVERHEAT -> "Hot"
                BATTERY_HEALTH_UNKNOWN -> "N/A"
                else -> "FAIL"
            }
        }

    val voltage: Double
        get() {
            val volt = batteryIntent!!.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0).toDouble()
            return java.lang.Double.valueOf(DecimalFormat("#.##").format(volt / 1000.0))
        }

    val temperature: Double
        get() {
            return (batteryIntent!!.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10).toDouble()
        }

    fun getVoltage(pattern: String): Double {
        val volt = voltage
        return java.lang.Double.valueOf(DecimalFormat(pattern).format(volt / 1000.0))
    }

    companion object {
        /**
         * Values indicating the live time of the day.
         */
        val LIVE_TIME_MORNING = 0
        val LIVE_TIME_AFTERNOON = 1
        val LIVE_TIME_EVENING = 2
        val LIVE_TIME_NIGHT = 3

        /**
         * Values indicating the plugged state.
         */
        val PLUGGED_STATE_AC = 0
        val PLUGGED_STATE_USB = 1
        val PLUGGED_STATE_WIRELESS = 2
        val PLUGGED_STATE_UNKNOWN = 3

        /**
         * Values for Extras.
         */
        val DEGREE = "\u00b0"

        /**
         * Values for indicating battery health.
         */
        val BATTERY_HEALTH_COLD = 0
        val BATTERY_HEALTH_DEAD = 1
        val BATTERY_HEALTH_GOOD = 2
        val BATTERY_HEALTH_OVER_VOLTAGE = 3
        val BATTERY_HEALTH_OVERHEAT = 4
        val BATTERY_HEALTH_UNKNOWN = 5
        val BATTERY_STATUS_FAILURE = -1
    }

}
