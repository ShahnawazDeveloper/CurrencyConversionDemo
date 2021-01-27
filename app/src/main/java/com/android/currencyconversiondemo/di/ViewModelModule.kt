package com.android.currencyconversiondemo.di

import com.android.currencyconversiondemo.presentation.converter.CurrencyConverterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Injecting ViewModel
 */

val viewModelModule = module {
    viewModel {
        CurrencyConverterViewModel()
    }

}