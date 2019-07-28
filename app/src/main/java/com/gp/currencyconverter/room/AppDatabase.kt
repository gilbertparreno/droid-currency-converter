package com.droid.hp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.droid.hp.room.dao.BalanceDao
import com.droid.hp.room.entities.TransactionHistory
import com.droid.hp.room.entities.Balance
import com.droid.hp.room.entities.Currency
import com.gp.currencyconverter.room.converters.Converters

@Database(entities = [Balance::class, TransactionHistory::class, Currency::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceDao(): BalanceDao
}