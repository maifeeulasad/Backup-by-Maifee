package com.mua.backupbymaifee.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class File(@PrimaryKey val absolutePath: String, val uploaded: Boolean = false)