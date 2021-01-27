package com.android.currencyconversiondemo.data.models

data class Currency(val code: String, val name: String){

    override fun toString(): String {
        return name
    }
}
