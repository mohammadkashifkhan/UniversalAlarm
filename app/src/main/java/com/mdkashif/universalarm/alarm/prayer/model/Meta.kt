package com.mdkashif.universalarm.alarm.prayer.model


import com.google.gson.annotations.SerializedName


data class Meta(

        @field:SerializedName("method")
        val method: Method? = null,

        @field:SerializedName("offset")
        val offset: Offset? = null,

        @field:SerializedName("school")
        val school: String? = null,

        @field:SerializedName("timezone")
        val timezone: String? = null,

        @field:SerializedName("midnightMode")
        val midnightMode: String? = null,

        @field:SerializedName("latitude")
        val latitude: Double? = null,

        @field:SerializedName("longitude")
        val longitude: Double? = null,

        @field:SerializedName("latitudeAdjustmentMethod")
        val latitudeAdjustmentMethod: String? = null
)