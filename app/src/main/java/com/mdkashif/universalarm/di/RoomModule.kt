package com.mdkashif.universalarm.di

import android.app.Application
import androidx.room.Room
import com.mdkashif.universalarm.persistence.AppDatabase
import com.mdkashif.universalarm.persistence.RoomAccessDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomDb(app: Application): AppDatabase = Room.databaseBuilder(app, AppDatabase::class.java, "universal-alarm-db")
            .build()


    @Provides
    @Singleton
    fun provideRoomAccessDao(appDatabase: AppDatabase): RoomAccessDao = appDatabase.accessDao()

}