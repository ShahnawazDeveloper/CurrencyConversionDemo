package com.android.currencyconversiondemo.utils

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData


fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, androidx.lifecycle.Observer(body))

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.visible(visible: Boolean) {
    if (visible) {
        visible()
    } else {
        gone()
    }
}
