package com.mdkashif.universalarm.di

import com.mdkashif.universalarm.alarm.location.ui.SetLocationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SetLocationFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeSetLocationFragment(): SetLocationFragment
}