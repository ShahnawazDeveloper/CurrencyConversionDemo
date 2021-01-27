package com.android.currencyconversiondemo.data.api

import com.android.currencyconversiondemo.data.models.CurrencyListResponse
import com.android.currencyconversiondemo.data.models.CurrencyLiveResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    // http://api.currencylayer.com/live?access_key=1b791ce89e94ff3c83cb98a79813028e
    @GET("live")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getLiveCurrencyQuote(@Query("access_key") accessKey: String): CurrencyLiveResponse

    // http://api.currencylayer.com/list?access_key=1b791ce89e94ff3c83cb98a79813028e
    @GET("list")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getCountryList(@Query("access_key") accessKey: String): CurrencyListResponse

    //(entererdvalue * basecurrency) / selectedBaseCurrency
}