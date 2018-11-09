package com.mdkashif.alarm.alarm.prayer.pojos


import com.google.gson.annotations.SerializedName


data class Method(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("params")
	val params: Params? = null
)