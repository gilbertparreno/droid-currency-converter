package com.gp.currencyconverter.app

import android.app.Application
import com.gp.currencyconverter.BuildConfig
import com.gp.currencyconverter.di.AppComponent
import com.gp.currencyconverter.di.AppModule
import com.gp.currencyconverter.di.DaggerAppComponent
import com.gp.currencyconverter.di.NetworkModule
import timber.log.Timber


class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule("http://api.evp.lt/currency/", BuildConfig.DEBUG))
            .appModule(AppModule(this))
            .build()
    }
}