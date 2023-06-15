package com.athar.bakeacademy

import com.google.gson.annotations.SerializedName

data class BookmarkResponse(

	@field:SerializedName("bookmark")
	val bookmark: List<BookmarkItem>
)

data class BookmarkItem(

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("id_resep")
	val idResep: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("id_user")
	val idUser: Int,

	@field:SerializedName("tanggal")
	val tanggal: String,

	@field:SerializedName("nama_roti")
	val namaRoti: String
)
