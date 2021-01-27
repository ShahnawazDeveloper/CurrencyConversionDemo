package com.android.currencyconversiondemo

import android.app.Application
import com.android.currencyconversiondemo.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(arrayListOf(networkModule, preferenceModule, repositoryModule, viewModelModule))
        }
    }
}