package org.ronil.hissab.models

import androidx.room.PrimaryKey

data class UserModel(
    val name: String,
    val createdData: String,
    val contactNum: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)