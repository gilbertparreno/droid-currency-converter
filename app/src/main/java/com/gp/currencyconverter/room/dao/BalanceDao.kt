package com.droid.hp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.droid.hp.room.entities.TransactionHistory
import com.droid.hp.room.entities.BalanceTransactionHistory
import com.droid.hp.room.entities.Balance
import com.droid.hp.room.entities.Currency

@Dao
interface BalanceDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertBalance(entity: Balance): Long

    @Query("SELECT * FROM balance WHERE id = :id")
    fun getTransactionHistory(id: Int): BalanceTransactionHistory

    @Query("SELECT * FROM balance WHERE currencyId = :currencyId")
    fun getBalanceByCurrencyCode(currencyId: Int): BalanceTransactionHistory

    @Query("SELECT * FROM balance")
    fun getAvailableBalances(): List<BalanceTransactionHistory>

    @Query("SELECT COUNT(*) FROM balance WHERE currencyId = :currencyId")
    fun getBalanceCount(currencyId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(entity: TransactionHistory): Long

    @Query("SELECT COUNT(*) FROM transactionHistory")
    fun getTransactionCount(): Int

    @Query("UPDATE balance SET balance = balance - :lessBalance WHERE id = :id")
    fun decreaseBalance(lessBalance: Double, id: Int): Int

    @Query("UPDATE balance SET balance = balance + :addBalance WHERE id = :id")
    fun increaseBalance(addBalance: Double, id: Int): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCurrencies(currencies: List<Currency>) : List<Long>

    @Query("SELECT * FROM currency")
    fun getCurrencies() : List<Currency>
}