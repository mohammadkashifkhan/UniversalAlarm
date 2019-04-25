package com.mdkashif.universalarm.di

import com.mdkashif.universalarm.alarm.prayer.ui.SetPrayerTimeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SetPrayerTimeFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeSetPrayerTimeFragment(): SetPrayerTimeFragment
}