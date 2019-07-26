package com.gp.base.app

import android.app.Application
import com.gp.base.BuildConfig
import com.gp.base.di.AppComponent
import com.gp.base.di.AppModule
import com.gp.base.di.DaggerAppComponent
import com.gp.base.di.NetworkModule
import timber.log.Timber


class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule("https://api.github.com", BuildConfig.DEBUG))
            .appModule(AppModule(this))
            .build()
    }
}