package com.gp.currencyconverter.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Currency(
    @SerializedName("amount")
    val transferredAmount: String = "",
    @SerializedName("currency")
    val destinationCurrency: String = "",
    @Expose(serialize = false, deserialize = false)
    var sourceCurrency: String?,
    @Expose(serialize = false, deserialize = false)
    var commissionFee: Double?,
    @Expose(serialize = false, deserialize = false)
    var baseAmount: Double?
)