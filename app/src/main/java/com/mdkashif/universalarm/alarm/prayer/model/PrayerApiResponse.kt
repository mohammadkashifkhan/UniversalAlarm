package com.mdkashif.universalarm.alarm.prayer.model

import com.google.gson.annotations.SerializedName

data class PrayerApiResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)