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
            builder.setMinimumLatency(60000) // every 1 minute
        else
            builder.setPeriodic(60000)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // requires unMetered network
        builder.setPersisted(true)
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }
}
