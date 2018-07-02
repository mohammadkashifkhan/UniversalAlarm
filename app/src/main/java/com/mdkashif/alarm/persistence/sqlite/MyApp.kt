package com.mdkashif.alarm.persistence.sqlite

import android.app.Application
import android.arch.persistence.room.Room

class MyApp  : Application() {

    companion object {
        var database: MyDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        MyApp.database =  Room.databaseBuilder(this, MyDatabase::class.java, "we-need-db").build()
    }
}