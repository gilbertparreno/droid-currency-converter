package com.gp.currencyconverter.network.service

import com.gp.currencyconverter.network.model.Currency
import com.gp.currencyconverter.network.model.Project
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface CurrencyService {

    @GET("commercial/exchange/{fromCurrency}/{toCurrency}/latest")
    fun convertCurrency(@Path("fromCurrency") fromCurrency: String, @Path("toCurrency") toCurrency: String): Single<Currency>
}