package com.mdkashif.universalarm.utils.di

import com.mdkashif.universalarm.alarm.prayer.misc.ApiInterface
import com.mdkashif.universalarm.utils.AppConstants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(AppConstants.GET_PRAYER_DATA)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideRetrofitApiInterface(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)
}