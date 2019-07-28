package com.gp.currencyconverter.screen.main

import androidx.lifecycle.*
import com.droid.hp.room.entities.Balance
import com.droid.hp.room.entities.BalanceTransactionHistory
import com.droid.hp.room.entities.Currency
import com.droid.hp.room.entities.TransactionHistory
import com.gp.currencyconverter.network.model.ApiResponse
import com.gp.currencyconverter.network.repository.CurrencyConverterRepositoryInteractor
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import org.threeten.bp.OffsetDateTime
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(private val interactor: CurrencyConverterRepositoryInteractor) :
        ViewModel(),
        LifecycleObserver {

    private val mutableLiveDataBalanceTransactionHistorySet = MutableLiveData<MutableSet<BalanceTransactionHistory>>()
            .apply {
                value = mutableSetOf()
            }
    private val mutableLiveDataConvertCurrency = MutableLiveData<ApiResponse<com.gp.currencyconverter.network.model.Currency>>()
    private val mutableLiveDataCurrencies = MutableLiveData<ApiResponse<List<Currency>>>()
    private val mutableLiveDataBalanceHistory = MutableLiveData<ApiResponse<BalanceTransactionHistory>>()
    private val mutableLiveDataTransactionHistory = MutableLiveData<ApiResponse<TransactionHistory>>()
    private val mutableLiveDataProgress = MutableLiveData<Boolean>()

    private val commissionPercent = 0.007

    private val disposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        // TODO onCreate
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposable.clear()
    }

    fun getLiveDataConvertCurrency(): LiveData<ApiResponse<com.gp.currencyconverter.network.model.Currency>> {
        return mutableLiveDataConvertCurrency
    }

    fun getLiveDataCurrencies(): LiveData<ApiResponse<List<Currency>>> {
        return mutableLiveDataCurrencies
    }

    fun getLiveDataBalanceTransactionHistorySet(): LiveData<MutableSet<BalanceTransactionHistory>> {
        return mutableLiveDataBalanceTransactionHistorySet
    }

    fun getLiveDataBalanceHistory(): LiveData<ApiResponse<BalanceTransactionHistory>> {
        return mutableLiveDataBalanceHistory
    }

    fun getLiveDataTransactionHistory(): LiveData<ApiResponse<TransactionHistory>> {
        return mutableLiveDataTransactionHistory
    }

    fun getLiveDataProgress(): LiveData<Boolean> {
        return mutableLiveDataProgress
    }

    fun convertCurrency(amount: Double, from: String, to: String, id: Int, currencyId: Int) {
        val remainingBalance = getLiveDataBalanceHistory().value?.data?.balance?.balance?.let {
            it
        } ?: 0.0
        when {
            from == to -> {
                mutableLiveDataConvertCurrency.postValue(ApiResponse(throwable = Throwable("Currencies are identical.")))
                return
            }
            amount <= 0 -> {
                mutableLiveDataConvertCurrency.postValue(ApiResponse(throwable = Throwable("Cannot convert zero and negative transferredAmount.")))
                return
            }
            amount > remainingBalance -> {
                mutableLiveDataConvertCurrency.postValue(ApiResponse(throwable = Throwable("Insufficient balance.")))
                return
            }
        }
        disposable.add(
                interactor.getBalanceTransactionHistoryCount().map { transactionCount ->
                    if (transactionCount > 5) {
                        val totalDeduction = amount + amount * commissionPercent

                        when {
                            totalDeduction > remainingBalance -> throw Throwable("Insufficient balance.")
                            else -> amount * 0.007
                        }
                    } else {
                        0.0
                    }
                }.flatMap { commissionFee ->
                    interactor.convertCurrency("$amount-$from", to)
                            .doOnSubscribe {
                                mutableLiveDataProgress.postValue(true)
                            }
                            .flatMap { currency ->
                                interactor.insertBalance(Balance(currencyId = currencyId, balance = 0.0))
                                        .map { newAddedBalanceId ->
                                            Pair(currency, newAddedBalanceId)
                                        }
                            }.flatMap { pairAddedBalance ->
                                interactor.getBalanceTransactionHistoryId(pairAddedBalance.second).map { newAddedBalance ->
                                    mutableLiveDataBalanceTransactionHistorySet.value?.add(newAddedBalance)
                                    Pair(pairAddedBalance.first, newAddedBalance.balance.id)
                                }
                            }.flatMap { pairAddedBalance ->
                                mutableLiveDataConvertCurrency.postValue(ApiResponse(pairAddedBalance.first.also { currency ->
                                    currency.commissionFee = commissionFee
                                    currency.sourceCurrency = from
                                    currency.baseAmount = amount
                                }))
                                Single.zip(interactor.insertTransaction(
                                        TransactionHistory(
                                                0,
                                                pairAddedBalance.first.destinationCurrency,
                                                amount,
                                                pairAddedBalance.first.transferredAmount.toDouble(),
                                                commissionFee,
                                                id,
                                                OffsetDateTime.now()
                                        )
                                ), interactor.decreaseBalance(amount + commissionFee, id).toSingleDefault(true),
                                        interactor.increaseBalance(
                                                pairAddedBalance.first.transferredAmount.toDouble(),
                                                pairAddedBalance.second
                                        ).toSingleDefault(
                                                true
                                        ),
                                        Function3<TransactionHistory, Boolean, Boolean, TransactionHistory> { transactionHistory, _, _ ->
                                            return@Function3 transactionHistory
                                        })
                            }
                }
                        .subscribe({ transactionHistory ->
                            mutableLiveDataProgress.postValue(false)
                            mutableLiveDataTransactionHistory.postValue(ApiResponse(transactionHistory))
                        }, { throwable ->
                            Timber.e(throwable)
                            mutableLiveDataProgress.postValue(false)
                            mutableLiveDataTransactionHistory.postValue(ApiResponse(throwable = throwable))
                        })
        )
    }

    fun insertAndGetBalance(balance: Balance, selectedBalanceId: Int) {
        disposable.add(
                if (selectedBalanceId == 0) {
                    interactor.insertBalance(balance).flatMap {
                        interactor.getBalanceTransactionHistoryId(it).map { latestBalance ->
                            listOf(latestBalance)
                        }
                    }
                } else {
                    interactor.getAvailableBalances()
                }
                        .subscribe(
                                { balanceHistory ->
                                    mutableLiveDataBalanceTransactionHistorySet.value?.addAll(balanceHistory)
                                    if (balanceHistory.size == 1) {
                                        mutableLiveDataBalanceHistory.postValue(ApiResponse(balanceHistory[0]))
                                    } else {
                                        mutableLiveDataBalanceHistory.postValue(ApiResponse(balanceHistory.single {
                                            it.balance.id == selectedBalanceId
                                        }))
                                    }
                                }, { throwable ->
                            Timber.e(throwable)
                            mutableLiveDataBalanceHistory.postValue(ApiResponse(throwable = throwable))
                        }
                        )
        )
    }

    fun getLatestBalance(id: Int = 1) {
        disposable.add(
                interactor.getBalanceTransactionHistoryId(id)
                        .subscribe({ balanceHistory ->
                            mutableLiveDataBalanceHistory.postValue(ApiResponse(balanceHistory))
                        }, { throwable ->
                            Timber.e(throwable)
                            mutableLiveDataBalanceHistory.postValue(ApiResponse(throwable = throwable))
                        })
        )
    }

    fun insertCurrencies(currencies: List<Currency>) {
        disposable.add(
                interactor.insertCurrencies(currencies)
                        .subscribe({
                            Timber.d("currencies inserted")
                            getCurrencies()
                        }, { throwable ->
                            Timber.e(throwable)
                        })
        )
    }

    private fun getCurrencies() {
        disposable.add(
                interactor.getCurrencies()
                        .subscribe({ currencies ->
                            mutableLiveDataCurrencies.postValue(ApiResponse(currencies))
                        }, { throwable ->
                            Timber.e(throwable)
                            mutableLiveDataCurrencies.postValue(ApiResponse(throwable = throwable))
                        })
        )
    }
}