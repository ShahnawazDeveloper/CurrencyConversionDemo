package com.android.currencyconversiondemo.di

import android.content.Context
import android.content.SharedPreferences
import com.android.currencyconversiondemo.R
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val preferenceModule = module {
    single { provideSharedPreference(androidApplication()) }
}

fun provideSharedPreference(context: Context): SharedPreferences {
    return context.getSharedPreferences(
        context.getString(R.string.app_name).replace("", "_"),
        Context.MODE_PRIVATE
    )
}
