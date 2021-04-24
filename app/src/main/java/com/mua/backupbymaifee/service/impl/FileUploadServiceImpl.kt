package com.mua.backupbymaifee.service.impl

import com.mua.backupbymaifee.manager.RetrofitManager
import com.mua.backupbymaifee.service.FileService
import okhttp3.MediaType
import okhttp3.MultipartBody

import okhttp3.RequestBody
import retrofit2.Callback
import java.io.File


class FileUploadServiceImpl {

    companion object {
        private val retrofit = RetrofitManager.newInstance()
        private val service = retrofit.create(FileService::class.java)

        fun upload(absolutePath: String, callback: Callback<Boolean>) {
            val file = File(absolutePath)
            val requestFile: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body =
                MultipartBody.Part.createFormData("file", file.name, requestFile)
            val name =
                RequestBody.create(MediaType.parse("multipart/form-data"), absolutePath)
            val call = service.upload(name, body)
            call.enqueue(callback)
        }

        fun upload(file: File, callback: Callback<Boolean>) {
            return upload(file.absolutePath, callback)
        }

        fun upload(file: com.mua.backupbymaifee.data.model.File, callback: Callback<Boolean>) {
            return upload(file.absolutePath, callback)
        }

    }


}