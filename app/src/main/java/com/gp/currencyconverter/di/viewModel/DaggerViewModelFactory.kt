package com.gp.currencyconverter.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class DaggerViewModelFactory @Inject constructor(private val viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
        ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = viewModelsMap[modelClass] ?:
        viewModelsMap.asIterable().firstOrNull { 
          modelClass.isAssignableFrom(it.key) 
        }?.value ?: throw IllegalArgumentException("Unknown model class $modelClass")
        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}