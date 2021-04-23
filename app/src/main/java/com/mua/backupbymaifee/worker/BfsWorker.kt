package com.mua.backupbymaifee.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File

class BfsWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val toScan = inputData.getStringArray("TO_SCAN")
        val res: ArrayList<String> = arrayListOf()
        Log.d("d--mua", "triggered for size" + toScan?.size)
        toScan?.forEach {
            val child = listChild(it)
            res.addAll(child)
        }
        val outputData =
            Data
                .Builder()
                .putStringArray("TO_SCAN", res.toTypedArray())
                .let { if (toScan != null) it.putStringArray("SCANNED", toScan) else it }
                .build()
        Log.d("d--mua", "returning for size" + res.size)
        return Result.success(outputData)
    }

    private fun listChild(path: String): MutableList<String> {
        val file = File(path)
        if (!file.isDirectory || !file.canRead()) {
            mutableListOf<String>()
        }
        Log.d("d--mua", "tri2 $path")
        val res: MutableList<String> = ArrayList()
        file.listFiles()?.forEach {
            res.add(it.absolutePath)
        }
        return res
    }

}