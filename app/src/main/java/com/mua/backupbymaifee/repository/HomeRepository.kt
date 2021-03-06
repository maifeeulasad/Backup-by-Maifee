package com.mua.backupbymaifee.repository

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.mua.backupbymaifee.data.AppDatabase
import com.mua.backupbymaifee.data.dao.FileDao
import com.mua.backupbymaifee.data.model.File
import com.mua.backupbymaifee.service.impl.FileUploadServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository(application: Application) {

    private val fileDao: FileDao

    val scanned: LiveData<List<File>>

    fun startScan(toScan: List<String>) {
        for (path in toScan) {
            val file = java.io.File(path)
            if (!file.canRead()) {
                continue
            }
            if (!file.isDirectory) {
                uploadToServer(File(file.absolutePath))
                GlobalScope.launch {
                    insertFile(File(file.absolutePath))
                }
            }
            file.listFiles()?.map {
                startScan(arrayListOf(it.absolutePath))
            }
        }
    }

    init {
        val db: AppDatabase = AppDatabase.getInstance(application)
        fileDao = db.fileDao()
        scanned = fileDao.getAll()
    }

    @WorkerThread
    suspend fun insertFile(file: File) = withContext(Dispatchers.IO) {
        fileDao.insert(file)
    }

    private fun uploadToServer(file: File) {
        FileUploadServiceImpl.upload(file.absolutePath, object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                GlobalScope.launch {
                    updateFile(file)
                }
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                GlobalScope.launch {
                    if (response.body() == null) {
                        updateFile(file, false)
                    } else {
                        updateFile(file, response.body()!!)
                    }
                }
            }

        })
    }

    @WorkerThread
    suspend fun updateFile(file: File, uploaded: Boolean = false) = withContext(Dispatchers.IO) {
        val temFile = File(file.absolutePath, uploaded)
        fileDao.update(temFile)
    }

}