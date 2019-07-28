package com.gp.currencyconverter.network.repository

import android.database.sqlite.SQLiteConstraintException
import com.droid.hp.room.dao.BalanceDao
import com.droid.hp.room.entities.Balance
import com.droid.hp.room.entities.BalanceTransactionHistory
import com.droid.hp.room.entities.TransactionHistory
import com.gp.currencyconverter.network.model.Currency
import com.gp.currencyconverter.network.service.CurrencyService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyConverterRepositoryInteractorImpl @Inject constructor(
    private val currencyService: CurrencyService,
    private val balanceDao: BalanceDao
) :
    CurrencyConverterRepositoryInteractor {

    override fun convertCurrency(fromCurrency: String, toCurrency: String): Single<Currency> {
        return currencyService.convertCurrency(fromCurrency, toCurrency)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertBalance(balance: Balance): Single<Int> {
        return Single.create<Int> { emitter ->
            try {
                val id = balanceDao.insertBalance(balance)
                emitter.onSuccess(id.toInt())
            } catch (e: SQLiteConstraintException) {
                // if existing just return its ID
                emitter.onSuccess(balanceDao.getBalanceByCurrencyCode(balance.currencyId).balance.id)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getBalanceTransactionHistoryId(id: Int): Single<BalanceTransactionHistory> {
        return Single.create<BalanceTransactionHistory> { emitter ->
            emitter.onSuccess(balanceDao.getTransactionHistory(id))
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getBalanceTransactionHistoryCount(): Single<Int> {
        return Single.create<Int> { emitter ->
            emitter.onSuccess(balanceDao.getTransactionCount())
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertTransaction(transactionHistory: TransactionHistory): Single<TransactionHistory> {
        return Single.create<TransactionHistory> { emitter ->
            val id = balanceDao.insertTransaction(transactionHistory)
            emitter.onSuccess(
                TransactionHistory(
                    id.toInt(),
                    transactionHistory.currencyCode,
                    transactionHistory.rawAmount,
                    transactionHistory.amount,
                    transactionHistory.commissionFee,
                    transactionHistory.balanceId,
                    transactionHistory.transactionDate
                )
            )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun decreaseBalance(lessBalance: Double, id: Int): Completable {
        return Completable.create { emitter ->
            balanceDao.decreaseBalance(lessBalance, id)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun increaseBalance(addBalance: Double, id: Int): Completable {
        return Completable.create { emitter ->
            balanceDao.increaseBalance(addBalance, id)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAvailableBalances(): Single<List<BalanceTransactionHistory>> {
        return Single.create<List<BalanceTransactionHistory>> { emitter ->
            val list = balanceDao.getAvailableBalances()
            if (list.isNotEmpty()) {
                emitter.onSuccess(list)
            } else {
                emitter.onError(Throwable("No balance available"))
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getCurrencies(): Single<List<com.droid.hp.room.entities.Currency>> {
        return Single.create<List<com.droid.hp.room.entities.Currency>> { emitter ->
            emitter.onSuccess(balanceDao.getCurrencies())
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertCurrencies(currencies: List<com.droid.hp.room.entities.Currency>): Completable {
        return Completable.create { emitter ->
            try {
                balanceDao.insertCurrencies(currencies)
                emitter.onComplete()
            } catch (e: SQLiteConstraintException) {
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}