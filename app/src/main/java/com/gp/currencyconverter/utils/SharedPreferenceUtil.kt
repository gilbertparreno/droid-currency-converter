package com.gp.currencyconverter.utils

import android.content.Context
import android.content.SharedPreferences
import com.gp.currencyconverter.BuildConfig

class SharedPreferenceUtil(val context: Context) {
    val PREFS_FILENAME = "${BuildConfig.APPLICATION_ID}.prefs"
    val SELECTED_BALANCE_ID = "selected_balance_id"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var selectedBalanceId: Int
        get() = prefs.getInt(SELECTED_BALANCE_ID, 0)
        set(value) = prefs.edit().putInt(SELECTED_BALANCE_ID, value).apply()
}