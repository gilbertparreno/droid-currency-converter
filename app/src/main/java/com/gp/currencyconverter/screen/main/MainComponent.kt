package com.gp.currencyconverter.screen.main

import com.gp.currencyconverter.di.AppComponent
import com.gp.currencyconverter.di.FragmentScope
import com.gp.currencyconverter.di.viewModel.ContainerViewModelModule
import com.gp.currencyconverter.viewModel.ViewModelFactoryModule
import dagger.Component

@FragmentScope
@Component(
    modules = [MainModule::class, ViewModelFactoryModule::class, ContainerViewModelModule::class],
    dependencies = [AppComponent::class]
)
interface MainComponent {
    fun inject(mainFragment: MainFragment)
}
