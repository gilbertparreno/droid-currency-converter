package com.droid.hp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName = "transactionHistory",
    foreignKeys = [ForeignKey(
        entity = Balance::class,
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("balanceId")
    )]
)
data class TransactionHistory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "currencyCode")
    val currencyCode: String,
    @ColumnInfo(name = "rawAmount")
    val rawAmount: Double,
    @ColumnInfo(name = "transferredAmount")
    val amount: Double,
    @ColumnInfo(name = "commissionFee")
    val commissionFee: Double = 0.0,
    @ColumnInfo(name = "balanceId")
    val balanceId: Int,
    @ColumnInfo(name = "transactionDate")
    val transactionDate: OffsetDateTime? = null
)