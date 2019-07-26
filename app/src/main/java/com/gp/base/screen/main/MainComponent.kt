package com.gp.base.screen.main

import com.gp.base.di.ActivityScope
import com.gp.base.di.AppComponent
import com.gp.base.viewModel.ViewModelFactoryModule
import com.gp.base.viewModel.ViewModelModule
import dagger.Component

@ActivityScope
@Component(
    modules = [MainModule::class, ViewModelFactoryModule::class, ViewModelModule::class],
    dependencies = [AppComponent::class]
)
interface MainComponent {
    fun inject(mainActivity: MainActivity)
}