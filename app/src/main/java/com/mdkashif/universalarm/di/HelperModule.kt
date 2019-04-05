package com.mdkashif.universalarm.di

import com.mdkashif.universalarm.alarm.location.misc.LocationHelper
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.persistence.RoomRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class HelperModule {

    @Provides
    @Singleton
    fun provideLocationHelper(appPreferences: AppPreferences, roomRepository: RoomRepository) = LocationHelper(appPreferences, roomRepository)
}