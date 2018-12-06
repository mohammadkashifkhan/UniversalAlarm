package com.mdkashif.alarm.alarm.prayer.model


import com.google.gson.annotations.SerializedName


data class Params(

	@field:SerializedName("Isha")
	val isha: Int? = null,

	@field:SerializedName("Fajr")
	val fajr: Int? = null
)