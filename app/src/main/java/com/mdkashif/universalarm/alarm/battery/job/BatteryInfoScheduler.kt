package com.mdkashif.universalarm.alarm.battery.job

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build


object BatteryInfoScheduler {
    fun scheduleJob(context: Context) {
        val serviceComponent = ComponentName(context, BatteryScheduleService::class.java)
        val builder = JobInfo.Builder(57586, serviceComponent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            builder.setMinimumLatency(900000) // 15 mins
        else
            builder.setPeriodic(900000)
        builder.setPersisted(true)
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }
}