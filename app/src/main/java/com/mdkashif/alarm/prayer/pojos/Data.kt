package com.mdkashif.alarm.prayer.pojos


import com.google.gson.annotations.SerializedName


data class Data(

	@field:SerializedName("date")
	val date: Date? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null,

	@field:SerializedName("timings")
	val timings: Timings? = null
)