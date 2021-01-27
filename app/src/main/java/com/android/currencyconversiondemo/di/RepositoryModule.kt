package com.android.currencyconversiondemo.di

import com.android.currencyconversiondemo.data.repository.CurrencyRepository
import com.android.currencyconversiondemo.data.repository.CurrencyRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


/**
 * Injecting repositories
 */
val repositoryModule = module {
    single<CurrencyRepository> { CurrencyRepositoryImpl(get()) }
}
