package com.gp.currencyconverter.network.model

import com.google.gson.annotations.SerializedName

data class CurrencyCode(
    @SerializedName("symbol")
    val symbol: String = "",
    @SerializedName("name_plural")
    val namePlural: String = "",
    @SerializedName("symbol_native")
    val symbolNative: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("decimal_digits")
    val decimalDigits: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("rounding")
    val rounding: Double = 0.0
)