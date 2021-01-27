package com.android.currencyconversiondemo.data.models

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

import com.google.gson.annotations.SerializedName


@Parcelize
data class CurrencyListResponse(
    @SerializedName("currencies")
    val currencies: HashMap<String, String>? = null
) : BaseModel(), Parcelable