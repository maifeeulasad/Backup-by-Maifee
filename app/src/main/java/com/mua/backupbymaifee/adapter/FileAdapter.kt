package com.mua.backupbymaifee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mua.backupbymaifee.data.model.File
import com.mua.backupbymaifee.databinding.ItemFileHomeFragmentBinding


class FileAdapter : RecyclerView.Adapter<FileAdapter.FilesVH>() {

    inner class FilesVH(val binding: ItemFileHomeFragmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesVH {
        val binding =
            ItemFileHomeFragmentBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return FilesVH(binding)
    }

    var files: Array<File> = arrayOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return files.size
    }

    override fun onBindViewHolder(holder: FilesVH, position: Int) {
        val file = files[position]
        holder.binding.apply {
            cbUploadedItemFileHomeFragment.isChecked = file.uploaded
            tvPathItemFileHomeFragment.text = file.absolutePath
        }
    }

}