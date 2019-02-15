package com.mdkashif.universalarm.alarm.prayer.job

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build

object PrayerDataFetchScheduler {
    fun scheduleJob(context: Context) {
        val serviceComponent = ComponentName(context, PrayerDataFetchScheduleService::class.java)
        val builder = JobInfo.Builder(98937, serviceComponent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            builder.setMinimumLatency(86400)
        else
            builder.setPeriodic(86400)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // require unmetered network
        builder.setPersisted(true)
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }
}
