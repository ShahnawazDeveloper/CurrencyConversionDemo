package com.android.currencyconversiondemo.presentation.core

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.currencyconversiondemo.R
import com.android.currencyconversiondemo.data.core.MyException

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    abstract fun getViewModel(): T
    abstract fun attachLiveData()


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onResume() {
        super.onResume()
        initExceptionHandler()
    }

    private fun initExceptionHandler() {
        getViewModel().errorLiveData.observe(this, Observer {
            getViewModel().changeProgress(false)
            it?.apply {
                if (it is MyException) {
                    when (it) {
                        is MyException.UnKnownError -> {
                            Toast.makeText(
                                this@BaseActivity,
                                getString(R.string.something_went_wrong),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is MyException.NetworkErrorError -> {
                            Toast.makeText(
                                this@BaseActivity,
                                getString(R.string.connection_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Log.e("Error", it.localizedMessage, it)
                }
                getViewModel().resetErrorLiveData()
            }
        })
        attachLiveData()
    }

    fun showToast(message: String?, duration: Int) {
        message?.let {
            Toast.makeText(this, message, duration).show()
        }
    }
}