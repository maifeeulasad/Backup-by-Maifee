package com.mua.backupbymaifee.viewmodel

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.work.*
import com.mua.backupbymaifee.worker.BfsWorker


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)

    //val toScan: MutableLiveData<List<String>> = MutableLiveData(arrayListOf())
    val toScan: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    val scanned : MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    //val toScan: LiveData<List<String>> = _toScan

    val workInfo: MutableLiveData<WorkInfo> = MutableLiveData()

    init {
        val mounts = mounts()
        //_toScan.postValue(mounts)
        toScan.postValue(mounts)
    }

    fun furtherBfs() {
        val inputData =
            Data
                .Builder()
                .putStringArray("TO_SCAN", toScan.value!!.toTypedArray())
                .build()
        val workRequest = OneTimeWorkRequestBuilder<BfsWorker>()
            .setInputData(inputData)
            .build()
        val worker = workManager
            .beginUniqueWork(
                "1",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        worker.enqueue()
        workInfo.postValue(workManager.getWorkInfoByIdLiveData(workRequest.id).value)
    }

    private fun mounts(): MutableList<String> {
        val res = mutableListOf<String>()
        val externalStorageFiles =
            ContextCompat.getExternalFilesDirs(getApplication(), null)
        val base = "/Android/data/" + getApplication<Application>().packageName + "/files"
        for (file in externalStorageFiles) {
            if (file != null) {
                val path = file.absolutePath
                if (path.contains(base)) {
                    val finalPath = path.replace(base, "")
                    res.add(finalPath)
                }
            }
        }
        return res
    }

}