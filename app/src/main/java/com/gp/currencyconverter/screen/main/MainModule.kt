package com.gp.currencyconverter.screen.main

import com.droid.hp.room.AppDatabase
import com.gp.currencyconverter.di.ActivityScope
import com.gp.currencyconverter.di.FragmentScope
import com.gp.currencyconverter.network.repository.CurrencyConverterRepositoryInteractor
import com.gp.currencyconverter.network.repository.CurrencyConverterRepositoryInteractorImpl
import com.gp.currencyconverter.network.service.CurrencyService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @Provides
    @FragmentScope
    fun providesCurrencyConverterRepositoryInteractor(retrofit: Retrofit, appDatabase: AppDatabase): CurrencyConverterRepositoryInteractor {
        return CurrencyConverterRepositoryInteractorImpl(retrofit.create(CurrencyService::class.java), appDatabase.balanceDao())
    }
}