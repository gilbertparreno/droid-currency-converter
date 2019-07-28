package com.gp.currencyconverter.network.repository

import com.droid.hp.room.entities.Balance
import com.droid.hp.room.entities.BalanceTransactionHistory
import com.droid.hp.room.entities.TransactionHistory
import com.gp.currencyconverter.network.model.Currency
import com.gp.currencyconverter.network.model.Project
import io.reactivex.Completable
import io.reactivex.Single

interface CurrencyConverterRepositoryInteractor {
    fun convertCurrency(fromCurrency: String, toCurrency: String): Single<Currency>
    fun insertBalance(balance: Balance): Single<Int>
    fun insertTransaction(transactionHistory: TransactionHistory): Single<TransactionHistory>
    fun getBalanceTransactionHistoryId(id: Int): Single<BalanceTransactionHistory>
    fun getBalanceTransactionHistoryCount(): Single<Int>
    fun decreaseBalance(lessBalance: Double, id: Int): Completable
    fun increaseBalance(addBalance: Double, id: Int): Completable
    fun getAvailableBalances(): Single<List<BalanceTransactionHistory>>
    fun getCurrencies() : Single<List<com.droid.hp.room.entities.Currency>>
    fun insertCurrencies(currencies: List<com.droid.hp.room.entities.Currency>) : Completable
}