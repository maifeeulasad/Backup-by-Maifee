package com.mua.backupbymaifee.repository

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.mua.backupbymaifee.data.AppDatabase
import com.mua.backupbymaifee.data.dao.FileDao
import com.mua.backupbymaifee.data.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    suspend fun insertFile(file: File) = withContext(Dispatchers.IO){
        fileDao.insert(file)
    }

}