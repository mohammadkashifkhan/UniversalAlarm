package com.mdkashif.universalarm.utils.di

import com.mdkashif.universalarm.activities.ContainerActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(activity: ContainerActivity)
}