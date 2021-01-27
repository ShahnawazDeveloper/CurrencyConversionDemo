package com.android.currencyconversiondemo.presentation.converter

import android.content.SharedPreferences
import android.os.Handler
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.currencyconversiondemo.data.core.map
import com.android.currencyconversiondemo.data.models.*
import com.android.currencyconversiondemo.data.models.Currency
import com.android.currencyconversiondemo.data.repository.CurrencyRepository
import com.android.currencyconversiondemo.presentation.core.BaseViewModel
import com.android.currencyconversiondemo.utils.PreferenceUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.koin.core.inject
import java.text.NumberFormat
import java.text.NumberFormat.getInstance
import java.util.*
import java.util.Currency.getInstance
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.abs

open class CurrencyConverterViewModel : BaseViewModel() {

    private val currencyRepository by inject<CurrencyRepository>()

    private val preferences: SharedPreferences by inject()

    @VisibleForTesting
    private val quotesLiveData by lazy { MutableLiveData<List<Quote>>() }
    private val currencyLiveData by lazy { MutableLiveData<List<Currency>>() }
    private val ratesLiveData by lazy { MutableLiveData<List<Rates>>() }

    private var convertJob: Job? = null

    private val countryMap: HashMap<String, String> = HashMap()

    private val MainSource = "USD"
    private val intervalTime: Long = 30

    // get currency list
    private fun getCurrencyFromCache() {
        PreferenceUtil.getCurrency(preferences)?.let {
            currencyLiveData.value = mapCurrencyData(it)
        } ?: kotlin.run {
            getCurrencyFromApi()
        }
    }

    private fun getCurrencyFromApi() {
        postData(currencyLiveData) {
            currencyRepository.getCurrencyList().map {
                PreferenceUtil.saveCurrency(preferences, it)
                mapCurrencyData(it)
            }
        }
    }

    private fun mapCurrencyData(mData: CurrencyListResponse): List<Currency> {
        mData.currencies?.let { map ->
            countryMap.putAll(map)
        }
        return mData.currencies.orEmpty().toList().map { Currency(it.first, it.second) }
            .sortedBy { it.name }
    }

    fun getCurrencyLiveData(): LiveData<List<Currency>> = currencyLiveData

    // get live currency quotes
    private fun getCurrencyQuoteFromCache() {
        PreferenceUtil.getQuotes(preferences)?.let {
            quotesLiveData.value = mapQuoteData(it)
        } ?: kotlin.run {
            getQuotesFromApi()
        }
    }

    private fun getQuotesFromApi() {
        postData(quotesLiveData) {
            currencyRepository.getLiveCurrencyQuote().map {
                PreferenceUtil.saveQuotes(preferences, it)
                PreferenceUtil.saveNextSyncTimeStamp(
                    preferences,
                    System.currentTimeMillis().plus(TimeUnit.MINUTES.toMillis(intervalTime))
                )
                mapQuoteData(it)
            }
        }
    }

    private fun mapQuoteData(mData: CurrencyLiveResponse): List<Quote> {
        return mData.quotes.orEmpty().toList().map { Quote(it.first, it.second) }
            .sortedBy { it.code }
    }

    fun getCurrencyQuotesLiveData(): LiveData<List<Quote>> = quotesLiveData

    fun convertRates(source: String, rate: Double) {
        changeProgress(true)
        convertJob?.cancel()
        val rateList: MutableList<Rates> = ArrayList()
        convertJob = viewModelScope.async {
            delay(500) // just for progress (can be removed)
            try {
                if (rate > 0) {
                    quotesLiveData.value?.let { quotesData ->
                        quotesData.firstOrNull { it.code.endsWith(source) }?.let {
                            quotesData.forEach { map ->
                                if (!map.code.endsWith(source)) {
                                    rateList.add(
                                        Rates(
                                            source = source,
                                            target = countryMap[map.code.replaceFirst(
                                                MainSource,
                                                ""
                                            )] ?: map.code,
                                            rate = NumberFormat.getInstance()
                                                .format(((map.value * rate) / it.value))
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                ratesLiveData.postValue(rateList)
            } catch (e: Exception) {
                (errorLiveData as MutableLiveData).postValue(e)
            } finally {
                changeProgress(false)
            }
        }
    }

    fun getRatesLiveData(): LiveData<List<Rates>> = ratesLiveData

    fun getCurrencyData() {
        val data = isLimitExceeded()
        if (!data.first) {
            getCurrencyFromCache()
            getCurrencyQuoteFromCache()
        }
        setTimerTask(data.second)
    }

    // for execute task at every 30 min
    private fun setTimerTask(initialDelay: Long) {
        val handler = Handler()
        val timer = Timer()
        val doAsynchronousTask: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post(Runnable {
                    try {
                        getCurrencyFromApi()
                        getQuotesFromApi()
                    } catch (e: Exception) {
                    }
                })
            }
        }
        timer.schedule(
            doAsynchronousTask,
            TimeUnit.MINUTES.toMillis(initialDelay),
            TimeUnit.MINUTES.toMillis(intervalTime)
        ) //execute in every 30 (1800000) minutes
    }

    // pair<isLimitExceed,Remaining time>
    private fun isLimitExceeded(): Pair<Boolean, Long> {
        val difference = TimeUnit.MILLISECONDS.toMinutes(
            System.currentTimeMillis() - PreferenceUtil.getNextSyncTimeStamp(preferences)
        )
        return if (difference < 0) {
            Pair(false, abs(difference))
        } else {
            Pair(true, 0)
        }
    }
}