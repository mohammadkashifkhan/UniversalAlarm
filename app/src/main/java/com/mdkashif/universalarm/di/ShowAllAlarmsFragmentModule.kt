package com.mdkashif.universalarm.di

import com.mdkashif.universalarm.alarm.misc.ui.ShowAllAlarmsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ShowAllAlarmsFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeShowAllAlarmsFragment(): ShowAllAlarmsFragment
}