package com.mua.backupbymaifee.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mua.backupbymaifee.data.model.File

@Dao
interface FileDao {
    @Query("select * from file")
    fun getAll(): LiveData<List<File>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(file: File)

    @Update
    suspend fun update(file: File)

}