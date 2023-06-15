package com.athar.bakeacademy

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("/login")
    fun getUser(
        @Field("username") username : String,
        @Field("password") password : String
    ) : Call<UserResponse>

    @FormUrlEncoded
    @POST("/register")
    fun postUser(
        @Field("nama") nama : String,
        @Field("username") username : String,
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<RegisterResponse>

    @GET("/recipes/{id}")
    fun getBakery(
        @Header("Authorization") token : String?,
        @Path("id") id : String?
    ) : Call<BakeryResponse>

    @GET("/bookmark/{id}")
    fun getBookmark(
        @Header("Authorization") token : String?,
        @Path("id") id : Int?
    ) : Call<BookmarkResponse>

    @GET("/bookmark/delete/{id}")
    fun deleteBookmark(
        @Header("Authorization") token : String?,
        @Path("id") id : Int?,

    ) : Call<RegisterResponse>

    @Multipart
    @POST("/update/profile/{id}")
    fun updateProfil(
        @Header("Authorization") token : String?,
        @Path("id") id : Int?,
        @Part nama : MultipartBody.Part?,
        @Part username : MultipartBody.Part?,
        @Part email : MultipartBody.Part?,
        @Part id_alergi : MultipartBody.Part,
        @Part file: MultipartBody.Part,
    ) : Call<UpdateProfilWithPhotoResponse>

    @Multipart
    @POST("/update/profile/{id}")
    fun updateProfilWithoutPicture(
        @Header("Authorization") token : String?,
        @Path("id") id : Int?,
        @Part nama : MultipartBody.Part?,
        @Part username : MultipartBody.Part?,
        @Part email : MultipartBody.Part?,
        @Part id_alergi : MultipartBody.Part,
    ) : Call<UpdateProfileResponse>

    @Multipart
    @POST("/upload")
    fun postImage(
        @Part file: MultipartBody.Part,
        @Header("Authorization") token : String?
    ) : Call<UploadImageResponse>

    @FormUrlEncoded
    @POST("/save/recipes")
    fun postBookmark(
        @Header("Authorization") token : String?,
        @Field("id_user") id_user : Int,
        @Field("id_resep") id_resep : Int,
        @Field("foto") foto : String,
    ) : Call<RegisterResponse>


}