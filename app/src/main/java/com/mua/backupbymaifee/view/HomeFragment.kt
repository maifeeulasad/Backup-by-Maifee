package com.mua.backupbymaifee.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mua.backupbymaifee.adapter.FileAdapter
import com.mua.backupbymaifee.data.model.File
import com.mua.backupbymaifee.databinding.FragmentHomeBinding
import com.mua.backupbymaifee.viewmodel.HomeViewModel


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()
    val filesAdapter = FileAdapter()
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
        init()
        return view
    }

    private fun init() {
        initBackupButton()
        initRecycleView()
    }

    private fun initBackupButton() {
        binding.btnBackup.setOnClickListener {
            viewModel.startScan()
        }
    }

    private fun initRecycleView() {
        binding.rvScanned.layoutManager = LinearLayoutManager(requireContext())
        binding.rvScanned.adapter = filesAdapter
        viewModel.scanned.observe(viewLifecycleOwner, Observer {
            filesAdapter.files = it.toTypedArray()
        })
    }

}