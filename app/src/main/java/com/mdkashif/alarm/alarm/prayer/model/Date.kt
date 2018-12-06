package com.mdkashif.alarm.alarm.prayer.model


import com.google.gson.annotations.SerializedName


data class Date(

	@field:SerializedName("readable")
	val readable: String? = null,

	@field:SerializedName("hijri")
	val hijri: Hijri? = null,

	@field:SerializedName("gregorian")
	val gregorian: Gregorian? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)