package com.mdkashif.universalarm.utils.di

import com.mdkashif.universalarm.activities.BaseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule{
    @ContributesAndroidInjector
    abstract fun contributeBaseActivity(): BaseActivity
}