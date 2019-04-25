package com.mdkashif.universalarm.di

import com.mdkashif.universalarm.alarm.battery.ui.SetBatteryLevelFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SetBatteryLevelFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeSetBatteryLevelFragment(): SetBatteryLevelFragment
}