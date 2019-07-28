package com.gp.currencyconverter.network.model

data class ApiResponse<T> (val data : T? = null, val throwable: Throwable? = null)