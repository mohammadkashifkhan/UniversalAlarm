package com.mdkashif.universalarm.alarm.prayer.model


import com.google.gson.annotations.SerializedName


data class Weekday(

        @field:SerializedName("ar")
        val ar: String? = null,

        @field:SerializedName("en")
        val en: String? = null
)