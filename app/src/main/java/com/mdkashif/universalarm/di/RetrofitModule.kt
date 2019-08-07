package com.mdkashif.universalarm.di

import com.mdkashif.universalarm.alarm.prayer.misc.ApiInterface
import com.mdkashif.universalarm.utils.AppConstants
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module(override = true) {
    single {
        Retrofit.Builder()
                .baseUrl(AppConstants.GET_PRAYER_DATA)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    single { (retrofit: Retrofit) -> retrofit.create(ApiInterface::class.java) }
}