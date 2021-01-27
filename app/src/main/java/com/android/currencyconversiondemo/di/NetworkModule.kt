package com.android.currencyconversiondemo.di

import android.content.Context
import com.android.currencyconversiondemo.BuildConfig
import com.android.currencyconversiondemo.data.api.ApiService
import com.android.currencyconversiondemo.utils.NetworkHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Koin module for networking (retrofit)
 */

val networkModule = module {
    single { provideNetworkHelper(androidContext()) }
    single { createOkHttpClient() }
    single { createRetrofit(get(), BuildConfig.BASE_URL) }
    single { createNetworkService<ApiService>(get()) }
}

private fun provideNetworkHelper(context: Context) = NetworkHelper(context)


fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
    }

    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

}

/*private fun provideApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)*/

inline fun <reified T> createNetworkService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}
