package com.mdkashif.universalarm.utils.di

import android.content.Context
import androidx.room.Room
import com.mdkashif.universalarm.utils.persistence.AppDatabase
import com.mdkashif.universalarm.utils.persistence.RoomAccessDao
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    @Inject
    fun provideRoomDb(context: Context): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "universal-alarm-db")
            .build()


    @Provides
    @Singleton
    @Inject
    fun provideRoomAccessDao(appDatabase: AppDatabase): RoomAccessDao = appDatabase.accessDao()

}