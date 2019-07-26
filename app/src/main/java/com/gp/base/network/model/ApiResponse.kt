package com.gp.base.network.model

data class ApiResponse<T> (val data : T? = null, val throwable: Throwable? = null)