package com.mdkashif.universalarm.di

import android.content.Context
import androidx.room.Room
import com.mdkashif.universalarm.persistence.AppDatabase
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.persistence.RoomRepository
import com.mdkashif.universalarm.utils.AppConstants
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val persistenceModule = module(override = true) {
    single { androidApplication().getSharedPreferences(AppConstants.spAlias, Context.MODE_PRIVATE) }

    single { AppPreferences() }

    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, AppConstants.dbAlias)
                .build()
    }

    single { get<AppDatabase>().accessDao() }

    single { RoomRepository() }
}