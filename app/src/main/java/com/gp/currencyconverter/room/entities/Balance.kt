package com.droid.hp.room.entities

import androidx.room.*

@Entity(
    tableName = "balance",
    indices = [Index(value = ["currencyId"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = Currency::class,
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("currencyId")
    )]
)
data class Balance(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "currencyId")
    val currencyId: Int,
    @ColumnInfo(name = "balance")
    val balance: Double = 0.0
) {
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        other as Balance
        if (currencyId != other.currencyId) return false

        return true
    }
}