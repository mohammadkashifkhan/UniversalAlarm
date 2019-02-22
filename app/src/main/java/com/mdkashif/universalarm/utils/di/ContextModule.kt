package com.mdkashif.universalarm.utils.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context
}