package com.athar.bakeacademy

import com.google.gson.annotations.SerializedName

data class UploadImageResponse(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("message")
	val message: String
)
