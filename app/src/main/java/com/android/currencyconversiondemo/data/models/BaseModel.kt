package com.android.currencyconversiondemo.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BaseModel(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("terms")
    val terms: String? = null,
    @SerializedName("privacy")
    val privacy: String? = null,
    @SerializedName("error")
    val error: Error? = null,
) : Parcelable


@Parcelize
data class Error(
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("info")
    val info: String? = null
) : Parcelable
