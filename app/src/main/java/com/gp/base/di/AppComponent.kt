package com.gp.base.di

import android.app.Application
import com.gp.base.app.App
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class])
interface AppComponent {
    fun inject(app: App)
    fun application(): Application
    fun retrofit(): Retrofit
}