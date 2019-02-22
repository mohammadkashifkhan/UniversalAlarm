package com.mdkashif.universalarm.utils.di

import android.app.Application
import androidx.room.Room
import com.mdkashif.universalarm.utils.persistence.AppDatabase
import com.mdkashif.universalarm.utils.persistence.RoomAccessDao
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