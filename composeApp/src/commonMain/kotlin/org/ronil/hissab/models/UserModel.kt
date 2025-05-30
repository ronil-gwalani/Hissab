package org.ronil.hissab.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ronil.hissab.utils.getCurrentDate
import org.ronil.hissab.utils.getCurrentTime

@Entity
data class UserModel(
    val name: String,
    val totalTransaction: Double = 0.0,
    val currentStatus: Double = 0.0,
    val createdDate: String = getCurrentDate(),
    val createdTime: String = getCurrentTime(),
    val contactNum: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)