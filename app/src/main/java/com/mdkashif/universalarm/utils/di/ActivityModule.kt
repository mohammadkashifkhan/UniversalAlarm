package com.mdkashif.universalarm.utils.di

import com.mdkashif.universalarm.base.BaseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeBaseActivity(): BaseActivity
}