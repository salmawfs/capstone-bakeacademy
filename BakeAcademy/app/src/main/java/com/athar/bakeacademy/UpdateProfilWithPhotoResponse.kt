package com.athar.bakeacademy

import com.google.gson.annotations.SerializedName

data class UpdateProfilWithPhotoResponse(

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
