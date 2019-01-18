package com.mdkashif.alarm.alarm.prayer.job;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.mdkashif.alarm.alarm.prayer.misc.PrayerPresenter;
import com.mdkashif.alarm.alarm.prayer.model.PrayerApiResponse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DataFetchJobService extends JobService implements PrayerPresenter.PrayerViewCallback {

    @Override
    public boolean onStartJob(JobParameters params) {
//        Util.scheduleJob(getApplicationContext()); // reschedule the job
//        new PrayerPresenter(this, SharedPrefHolder.getInstance(getApplicationContext()).getCity(),
//                SharedPrefHolder.getInstance(getApplicationContext()).getCountry()).getPrayerDetails();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    @Override
    public void onPrayerDetailSuccess(@Nullable PrayerApiResponse prayerApiResponse) {
        // TODO: insert in db
    }

    @Override
    public void onError(@NotNull String error) {
        // do nothing
    }
}
