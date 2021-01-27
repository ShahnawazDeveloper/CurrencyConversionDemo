package com.android.currencyconversiondemo.data.repository

import com.android.currencyconversiondemo.BuildConfig
import com.android.currencyconversiondemo.data.api.ApiService
import com.android.currencyconversiondemo.data.core.BaseRepository
import com.android.currencyconversiondemo.data.core.Either
import com.android.currencyconversiondemo.data.core.MyException
import com.android.currencyconversiondemo.data.models.CurrencyListResponse
import com.android.currencyconversiondemo.data.models.CurrencyLiveResponse
interface CurrencyRepository {
    suspend fun getLiveCurrencyQuote(): Either<MyException, CurrencyLiveResponse>
    suspend fun getCurrencyList(): Either<MyException, CurrencyListResponse>
}

class CurrencyRepositoryImpl(
    private val apiService: ApiService
) : BaseRepository(),
    CurrencyRepository {
    override suspend fun getLiveCurrencyQuote() = executeSafeApiCall {
        apiService.getLiveCurrencyQuote(BuildConfig.ACCESS_KEY)
    }

    override suspend fun getCurrencyList() = executeSafeApiCall {
        apiService.getCountryList(BuildConfig.ACCESS_KEY)
    }
}


