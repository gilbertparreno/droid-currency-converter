package com.droid.hp.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class BalanceTransactionHistory(
    @Embedded val balance: Balance,
    @Relation(
        parentColumn = "id",
        entityColumn = "balanceId",
        entity = TransactionHistory::class
    )
    val transactions: List<TransactionHistory>
)