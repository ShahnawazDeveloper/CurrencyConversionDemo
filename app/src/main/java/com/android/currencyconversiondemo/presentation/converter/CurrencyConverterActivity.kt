package com.android.currencyconversiondemo.presentation.converter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.android.currencyconversiondemo.R
import com.android.currencyconversiondemo.data.models.Currency
import com.android.currencyconversiondemo.presentation.converter.adapter.CurrencyRatesAdapter
import com.android.currencyconversiondemo.presentation.core.BaseActivity
import com.android.currencyconversiondemo.utils.CommonUtils
import com.android.currencyconversiondemo.utils.gone
import com.android.currencyconversiondemo.utils.observe
import com.android.currencyconversiondemo.utils.visible
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyConverterActivity : BaseActivity<CurrencyConverterViewModel>() {
    private val currencyConverterViewModel by viewModel<CurrencyConverterViewModel>()
    private var selectedCurrency: Currency? = null
    private var currencyRatesAdapter: CurrencyRatesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //getViewModel().getCurrency()
        getViewModel().getCurrencyData()
        initListeners()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rvCurrency.layoutManager = GridLayoutManager(this, 3)
        currencyRatesAdapter = CurrencyRatesAdapter()
        rvCurrency.adapter = currencyRatesAdapter
    }

    private fun initListeners() {
        etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                getCurrencyRates(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun getViewModel(): CurrencyConverterViewModel = currencyConverterViewModel

    override fun attachLiveData() {
        observe(getViewModel().progressLiveData) {
            it?.let {
                progress.visible(it)
            }
        }

        observe(getViewModel().errorLiveData) {
            showToast(it?.message, Toast.LENGTH_SHORT)
        }

        observe(getViewModel().getCurrencyLiveData()) {
            it?.apply {
                setupCurrency(this)
            }
        }

        observe(getViewModel().getRatesLiveData()) {
            it?.let {
                currencyRatesAdapter?.setRatesList(it)
            }
        }
    }

    private fun setupCurrency(list: List<Currency>) {
        actvCurrency.setAdapter(ArrayAdapter(this, android.R.layout.simple_spinner_item, list))
        actvCurrency.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            selectedCurrency = actvCurrency.adapter.getItem(position) as Currency
            getCurrencyRates(etAmount.text.toString())
            CommonUtils.hideKeyboard(this@CurrencyConverterActivity)
        }
    }

    fun getCurrencyRates(str: String?) {
        if (str.isNullOrEmpty()) {
            currencyRatesAdapter?.setRatesList(arrayListOf())
        } else {
            selectedCurrency?.let {
                getViewModel().convertRates(it.code, str.toString().toDouble())
            }
        }
    }
}