package com.mdkashif.universalarm.di

import com.mdkashif.universalarm.activities.ContainerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeContainerActivity(): ContainerActivity
}