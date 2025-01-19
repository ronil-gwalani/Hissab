package org.ronil.hissab.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserContacts(
    val name: String,
    val createdData: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
