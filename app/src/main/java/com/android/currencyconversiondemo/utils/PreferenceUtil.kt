package com.android.currencyconversiondemo.utils

import android.content.Context
import android.content.SharedPreferences
import com.android.currencyconversiondemo.R
import com.android.currencyconversiondemo.data.models.CurrencyListResponse
import com.android.currencyconversiondemo.data.models.CurrencyLiveResponse
import com.google.gson.Gson

object PreferenceUtil {
    @Synchronized
    fun saveCurrency(preferences: SharedPreferences, user: CurrencyListResponse?) {
        val editor = preferences.edit()
        val userData = Gson().toJson(user)
        editor.putString("CURRENCY_DATA", userData).apply()
    }

    fun getCurrency(preferences: SharedPreferences): CurrencyListResponse? {
        val userData = preferences.getString("CURRENCY_DATA", null)
        userData?.let {
            return Gson().fromJson(it, CurrencyListResponse::class.java)
        }
        return null
    }

    @Synchronized
    fun saveQuotes(preferences: SharedPreferences, user: CurrencyLiveResponse?) {
        val editor = preferences.edit()
        val userData = Gson().toJson(user)
        editor.putString("QUOTE_DATA", userData).apply()
    }

    fun getQuotes(preferences: SharedPreferences): CurrencyLiveResponse? {
        val userData = preferences.getString("QUOTE_DATA", null)
        userData?.let {
            return Gson().fromJson(it, CurrencyLiveResponse::class.java)
        }
        return null
    }

    @Synchronized
    fun saveNextSyncTimeStamp(preferences: SharedPreferences, timestamp: Long?) {
        val editor = preferences.edit()
        editor.putLong("TIMESTAMP", timestamp ?: 0).apply()
    }

    fun getNextSyncTimeStamp(preferences: SharedPreferences): Long {
        return preferences.getLong("TIMESTAMP", 0)
    }

    fun clearData(preferences: SharedPreferences) {
        preferences.edit().clear().apply()
    }
}
