package com.stenleone.stanleysfilm.viewModel.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    protected val apiService: ApiService,
    protected val sharedPreferencesManager: SharedPreferencesManager,
    protected val connectionManager: ConnectionManager
) : ViewModel() {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Main + viewModelJob)
    private var isActive = true

    val inProgress = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<RequestError>()
    val isFailure: (errorMessage: String, type: String) -> Unit = { errorMessage: String, type: String ->
        errorLiveData.postValue(RequestError(type = type, message = errorMessage))
    }

    fun <P> doWork(doOnAsyncBlock: suspend CoroutineScope.() -> P) {
        doCoroutineWork(doOnAsyncBlock, viewModelScope, IO)
    }

    fun <P> doAsyncRequest(doOnAsyncBlock: suspend CoroutineScope.() -> P) {
        try {
            inProgress.postValue(true)
            doCoroutineWork(doOnAsyncBlock, viewModelScope, IO)
        } catch (e: Exception) {
            if (connectionManager.isConnected.value == true) {
                isFailure(RequestError.REQUEST_ERROR, e.message.toString())
            } else {
                isFailure(RequestError.CONNECTION_ERROR, e.message.toString())
            }
        } finally {
            inProgress.postValue(false)
        }
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