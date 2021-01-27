package com.android.currencyconversiondemo.presentation.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.currencyconversiondemo.data.core.Either
import com.android.currencyconversiondemo.data.core.MyException
import org.koin.core.KoinComponent
import kotlinx.coroutines.*
import java.lang.Exception

open class BaseViewModel : ViewModel(), KoinComponent {

    /**
     * initialize as LiveData to limit scope
     */
    val progressLiveData: LiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val errorLiveData: LiveData<Exception> by lazy { MutableLiveData<Exception>() }

    /**
     * starts loader ,wors on viewmodel coroutine scope and post result on provided livedata
     * @param isProgress pass true if progress live data need to update to manage loaders while calling
     */

    fun <R> postData(
        observer: MutableLiveData<R>,
        isProgress: Boolean = true,
        result: suspend () -> Either<MyException, R>
    ) {

        //close if started by some other process
        updateProgressBar(isProgress,false)
        viewModelScope.launch {
            updateProgressBar(isProgress, true)
            result.invoke().either({
                updateProgressBar(isProgress, false)
                (errorLiveData as MutableLiveData).postValue(it)
            }, {
                updateProgressBar(isProgress, false)
                observer.postValue(it)
            })
        }
    }


    //  will be used to reset if once error is shown
    fun resetErrorLiveData() {
        (errorLiveData as MutableLiveData).postValue(null)
    }

    public fun changeProgress(progress: Boolean) {
        (progressLiveData as MutableLiveData).postValue(progress)
    }

    private fun updateProgressBar(isProgress: Boolean, progress: Boolean) {
        if (isProgress)
            changeProgress(progress)

    }
}