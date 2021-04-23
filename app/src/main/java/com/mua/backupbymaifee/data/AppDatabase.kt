package com.mua.backupbymaifee.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mua.backupbymaifee.data.dao.FileDao
import com.mua.backupbymaifee.data.model.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Database(entities = [File::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fileDao(): FileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "backup_by_maifee.db"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}