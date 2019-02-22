package com.mdkashif.universalarm.utils.di

import com.mdkashif.universalarm.BaseApplication
import com.mdkashif.universalarm.utils.persistence.RoomHelper
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, BuildersModule::class, ContextModule::class, SharedPrefsModule::class, RoomModule::class, RetrofitModule::class])
interface AppComponent {

    fun inject(app: BaseApplication)

//    fun inject(activity: BaseActivity)
//
    fun inject(roomHelper: RoomHelper)
//
//    fun inject(settingsActivity: SettingsActivity)
//
//    fun inject(appIntroActivity: AppIntroActivity)
//
//    fun inject(sharedPrefHolder: SharedPrefHolder)
//
//    fun inject(batteryLevelFragment: SetBatteryLevelFragment)
//
//    fun inject(prayerTimeFragment: SetPrayerTimeFragment)
//
//    fun inject(prayerDataFetchScheduler: PrayerDataFetchScheduler)
//
//    fun inject(batteryInfoScheduler: BatteryInfoScheduler)
}