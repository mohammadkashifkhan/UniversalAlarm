package com.mdkashif.universalarm.di

import com.mdkashif.universalarm.alarm.misc.ui.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment
}