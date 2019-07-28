package com.gp.currencyconverter.di.viewModel

import androidx.lifecycle.ViewModel
import com.gp.currencyconverter.screen.main.MainViewModel
import com.gp.currencyconverter.viewModel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContainerViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun containerViewModel(mainViewModel: MainViewModel): ViewModel
}
