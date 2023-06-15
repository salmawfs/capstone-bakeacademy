package com.athar.bakeacademy

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServicePredict {

    @Multipart
    @POST("/predict")
    fun getBakery(
        @Part image: MultipartBody.Part,
    ) : Call<PredictResponse>

}