package com.gp.currencyconverter.di

import android.app.Application
import androidx.room.Room
import com.droid.hp.room.AppDatabase
import com.gp.currencyconverter.utils.SharedPreferenceUtil
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {

    @Singleton
    @Provides
    fun providesApplication() : Application {
        return application
    }

    @Singleton
    @Provides
    fun providesRoomDatabase(): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java, "balance-db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideSharedPreferenceUtil(): SharedPreferenceUtil {
        return SharedPreferenceUtil(application)
    }
}