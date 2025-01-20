package org.ronil.hissab.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ronil.hissab.utils.getCurrentDate
import org.ronil.hissab.utils.getCurrentTime


@Entity
data class ExpenseModel(
    val friendsId: Int,
    val transactionAmount: Double,
    val type: ExpenseType,
    val createdDate: String = getCurrentDate(),
    val createdTime: String = getCurrentTime(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

enum class ExpenseType {
    GIVEN,
    TAKEN
}
