package com.android.currencyconversiondemo.presentation.converter.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.currencyconversiondemo.data.models.Rates

class RateDiffCallBack  : DiffUtil.ItemCallback<Rates>() {
    override fun areItemsTheSame(oldItem: Rates, newItem: Rates): Boolean {
        return newItem.target == oldItem.target
    }

    override fun areContentsTheSame(oldItem: Rates, newItem: Rates): Boolean {
        return newItem.rate == oldItem.rate
    }

}