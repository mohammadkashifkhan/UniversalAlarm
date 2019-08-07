package com.mdkashif.universalarm.di

import com.mdkashif.universalarm.alarm.location.misc.LocationHelper
import org.koin.dsl.module

val locationHelperModule = module(override = true) {
    single { LocationHelper() }
}