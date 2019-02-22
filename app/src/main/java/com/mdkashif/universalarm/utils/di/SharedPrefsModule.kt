package com.mdkashif.universalarm.utils.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class SharedPrefsModule {

    @Provides
    @Singleton
    @Inject
    fun provideSharedPrefs(context: Context): SharedPreferences = context.getSharedPreferences("universal-alarm-sp", Context.MODE_PRIVATE)
}