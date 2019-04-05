package com.mdkashif.universalarm.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.mdkashif.universalarm.persistence.AppDatabase
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.persistence.RoomAccessDao
import com.mdkashif.universalarm.persistence.RoomRepository
import com.mdkashif.universalarm.utils.AppConstants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences = app.getSharedPreferences(AppConstants.spAlias, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideAppPreferences(sharedPreferences: SharedPreferences) = AppPreferences(sharedPreferences)

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase = Room.databaseBuilder(app, AppDatabase::class.java, AppConstants.dbAlias)
            .build()

    @Provides
    @Singleton
    fun provideRoomAccessDao(appDatabase: AppDatabase): RoomAccessDao = appDatabase.accessDao()

    @Provides
    @Singleton
    fun provideRoomRepository(roomAccessDao: RoomAccessDao) = RoomRepository(roomAccessDao)
}