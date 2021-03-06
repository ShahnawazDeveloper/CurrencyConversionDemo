package com.android.currencyconversiondemo.data.core

sealed class MyException(val throwable: Throwable, val msg: String = "") : Exception() {

    public class UnKnownError(throwable: Throwable) : MyException(throwable)
    public class NetworkErrorError(throwable: Throwable) : MyException(throwable)
    public class ValidationException(throwable: Throwable, val stringId: Int) :
        MyException(throwable)
}
