package com.athar.bakeacademy

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("token")
	val token: String
)

data class User(

	@field:SerializedName("id_alergi")
	val idAlergi: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("exp")
	val exp: Int,

	@field:SerializedName("iat")
	val iat: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
