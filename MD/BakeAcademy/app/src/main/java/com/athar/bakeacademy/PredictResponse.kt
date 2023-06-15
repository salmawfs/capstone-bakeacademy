package com.athar.bakeacademy

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("predicted_class")
	val predictedClass: String,

	@field:SerializedName("accuracy")
	val accuracy: Float
)
