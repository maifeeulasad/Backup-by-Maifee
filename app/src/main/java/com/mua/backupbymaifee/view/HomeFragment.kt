package com.mua.backupbymaifee.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mua.backupbymaifee.databinding.FragmentHomeBinding
import com.mua.backupbymaifee.viewmodel.HomeViewModel
import com.mua.backupbymaifee.worker.BfsWorker


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()
    private val workManager = WorkManager.getInstance()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        observeToScan()
        return view
    }

    private fun observeToScan() {
        viewModel.toScan.observe(viewLifecycleOwner, Observer {
            if (it !== null && it.isNotEmpty()) {
                Log.d("d--mua", "triggering for size" + it.size)
                val inputData =
                    Data
                        .Builder()
                        .putStringArray("TO_SCAN", viewModel.toScan.value!!.toTypedArray())
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
                workManager.getWorkInfoByIdLiveData(workRequest.id)
                    .observe(viewLifecycleOwner, Observer {
                        if (it != null) {
                            val o = it.outputData.getStringArray("TO_SCAN")
                            val p = it.outputData.getStringArray("SCANNED")
                            if (o != null && p != null) {
                                val tt = viewModel.toScan.value
                                tt?.addAll(o.toList())
                                tt?.removeAll(p.toList())
                                viewModel.toScan.postValue(tt)
                                val tem = viewModel.scanned.value
                                tem?.addAll(o.toList())
                                viewModel.scanned.postValue(tem)
                            }
                        }
                    })
            }
        })
        viewModel.scanned.observe(viewLifecycleOwner, Observer {
            binding.tvScanned.text = it.joinToString()
        })
    }

}