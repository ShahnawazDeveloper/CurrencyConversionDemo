package com.android.currencyconversiondemo.data.models

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

import com.google.gson.annotations.SerializedName


@Parcelize
data class CurrencyLiveResponse(
    @SerializedName("timestamp")
    val timestamp: Long? = null,
    @SerializedName("source")
    val source: String? = null,
    @SerializedName("quotes")
    val quotes: HashMap<String, Double>? = null
) : BaseModel(), Parcelable