package com.mua.backupbymaifee.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface FileService {

    @Multipart
    @POST("file/upload/")
    fun upload(
        @Part("absolute_path") absolutePath: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<Boolean>

}