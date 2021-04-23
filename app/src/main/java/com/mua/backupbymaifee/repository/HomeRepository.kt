package com.mua.backupbymaifee.repository

import androidx.lifecycle.MutableLiveData
import java.io.File

class HomeRepository {

    val scanned: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    fun startScan(toScan: List<String>) {
        for (path in toScan) {
            val file = File(path)
            if (!file.canRead()) {
                continue
            }
            if (!file.isDirectory) {
                val temScanned = scanned.value
                temScanned?.add(file.absolutePath)
                scanned.postValue(temScanned)
            }
            file.listFiles()?.map {
                startScan(arrayListOf(it.absolutePath))
            }
        }
    }

}