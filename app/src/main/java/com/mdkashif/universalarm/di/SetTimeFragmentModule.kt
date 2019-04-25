package com.mdkashif.universalarm.di

import com.mdkashif.universalarm.alarm.time.ui.SetTimeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SetTimeFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeSetTimeFragment(): SetTimeFragment
}