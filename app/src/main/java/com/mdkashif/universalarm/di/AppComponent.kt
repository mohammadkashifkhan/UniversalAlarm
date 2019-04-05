package com.mdkashif.universalarm.di

import android.app.Application
import com.mdkashif.universalarm.base.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ActivityModule::class, FragmentModule::class, PersistenceModule::class, RetrofitModule::class, HelperModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: BaseApplication)
}