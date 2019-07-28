package com.droid.hp.room.entities

import androidx.room.*

@Entity(
    tableName = "currency",
    indices = [Index(value = ["symbol"], unique = true)]
)
data class Currency(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "name_plural")
    val name_plural: String
)