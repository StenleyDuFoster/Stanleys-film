package com.stenleone.stanleysfilm.viewModel.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Main + viewModelJob)
    private var isActive = true

    fun <P> doWork(doOnAsyncBlock: suspend CoroutineScope.() -> P) {
        doCoroutineWork(doOnAsyncBlock, viewModelScope, IO)
    }

    fun <P> doWorkInMainThread(doOnAsyncBlock: suspend CoroutineScope.() -> P) {
        doCoroutineWork(doOnAsyncBlock, viewModelScope, Main)
    }

    fun <P> doRepeatWork(delay: Long, doOnAsyncBlock: suspend CoroutineScope.() -> P) {
        isActive = true
        viewModelScope.launch {
            while (this@BaseViewModel.isActive) {
                withContext(IO) {
                    doOnAsyncBlock.invoke(this)
                }
                if (this@BaseViewModel.isActive) {
                    delay(delay)
                }
            }
        }
    }

    fun stopRepeatWork() {
        isActive = false
    }

    private inline fun <P> doCoroutineWork(
        crossinline doOnAsyncBlock: suspend CoroutineScope.() -> P,
        coroutineScope: CoroutineScope,
        context: CoroutineContext
    ) {
        coroutineScope.launch {
            withContext(context) {
                doOnAsyncBlock.invoke(this)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        isActive = false
        viewModelJob.cancel()
    }
}