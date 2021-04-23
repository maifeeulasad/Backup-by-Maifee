package com.mua.backupbymaifee.viewmodel

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mua.backupbymaifee.data.model.File
import com.mua.backupbymaifee.repository.HomeRepository


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val homeRepository = HomeRepository(application)

    private val toScan: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    val scanned: LiveData<List<File>> = homeRepository.scanned

    init {
        val mounts = mounts()
        toScan.postValue(mounts)
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


    fun startScan() {
        homeRepository.startScan(toScan.value!!)
    }

}