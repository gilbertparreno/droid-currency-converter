package com.gp.currencyconverter.di

import android.app.Application
import com.droid.hp.room.AppDatabase
import com.gp.currencyconverter.app.App
import com.gp.currencyconverter.utils.SharedPreferenceUtil
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class])
interface AppComponent {
    fun inject(app: App)
    fun application(): Application
    fun retrofit(): Retrofit
    fun appDatabase(): AppDatabase
    fun sharedPreferenceUtil(): SharedPreferenceUtil
}