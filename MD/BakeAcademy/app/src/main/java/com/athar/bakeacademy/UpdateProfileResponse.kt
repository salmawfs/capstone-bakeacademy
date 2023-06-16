package com.athar.bakeacademy

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
