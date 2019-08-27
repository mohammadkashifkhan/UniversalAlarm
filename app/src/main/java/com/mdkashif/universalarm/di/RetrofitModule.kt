package com.mdkashif.universalarm.di

import com.mdkashif.universalarm.alarm.prayer.misc.ApiInterface
import com.mdkashif.universalarm.utils.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module(override = true) {
    single {
        val httpClient = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpClient.addInterceptor(httpLoggingInterceptor.apply { httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY })
        Retrofit.Builder()
                .baseUrl(AppConstants.GET_PRAYER_DATA_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    single { get<Retrofit>().create(ApiInterface::class.java) }
}