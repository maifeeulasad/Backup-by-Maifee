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
        //Log.d("d--mua", toScan.toString())
        /*
        toScan!!.forEach {
            Log.d("d--mua", "$it passed")
        }
         */
        Log.d("d--mua", "triggered for size" + toScan?.size)
        toScan?.forEach {
            //Log.d("d--mua", "here1")
            Log.d("d--mua", "tri1 $it")
            val child = listChild(it)
            //Log.d("d--mua", child.size.toString() + " size^^")
            /*
            for (children in child) {
                Log.d("d--mua", "$children^^^")
            }
             */
            res.addAll(child)
        }
        //val outputData = workDataOf("TO_SCAN" to res)
        //val outputData = workDataOf("TO_SCAN" to arrayListOf("1","2"))
        /*
        for (x in res) {
            Log.d("d--mua", "$x*-*-*")
        }
         */
        val outputData =
            Data
                .Builder()
                .putStringArray("TO_SCAN", res.toTypedArray())
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
        /*
        val file = File(path)
        //if (file.isDirectory || file.canRead())
        if (true) {
            val list = file.listFiles()
            Log.d("d--mua", list.size.toString() + " size")
            return list?.map {
                Log.d("d--mua", "abs : " + it.absolutePath)
                it.absolutePath
            } as MutableList<String>? ?: mutableListOf()
        }
        return mutableListOf()
         */
    }

}