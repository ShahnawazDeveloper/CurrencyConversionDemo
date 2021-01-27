package com.android.currencyconversiondemo.presentation.converter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.android.currencyconversiondemo.R
import com.android.currencyconversiondemo.data.models.Rates
import kotlinx.android.synthetic.main.list_item_currency_rates.view.*

class CurrencyRatesAdapter() : RecyclerView.Adapter<CurrencyRatesAdapter.ViewHolder>() {

    private var ratesList: List<Rates>? = null

    private val differ: AsyncListDiffer<Rates> =
        object : AsyncListDiffer<Rates>(this, RateDiffCallBack()) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_currency_rates, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size //ratesList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView() {
            itemView.apply {
                val data = differ.currentList[adapterPosition]//ratesList?.get(adapterPosition)
                data?.let {
                    tvRates.text = ("${it.target}\n${it.rate}")
                }
            }

        }
    }

    fun setRatesList(ratesList: List<Rates>?) {
        differ.submitList(ratesList)
        /*this.ratesList = ratesList
        notifyDataSetChanged()*/
    }


}
