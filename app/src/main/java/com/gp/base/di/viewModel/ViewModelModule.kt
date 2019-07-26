package com.gp.base.viewModel

import androidx.lifecycle.ViewModel
import com.gp.base.screen.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(deliveriesViewModel: MainViewModel): ViewModel
}