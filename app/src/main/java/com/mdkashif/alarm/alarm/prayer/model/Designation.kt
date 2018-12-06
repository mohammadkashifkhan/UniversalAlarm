package com.mdkashif.alarm.alarm.prayer.model


import com.google.gson.annotations.SerializedName


data class Designation(

	@field:SerializedName("expanded")
	val expanded: String? = null,

	@field:SerializedName("abbreviated")
	val abbreviated: String? = null
)