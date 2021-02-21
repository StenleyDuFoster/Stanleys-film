package com.stenleone.stanleysfilm.viewModel.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class RateMovieViewModel @Inject constructor(
    apiService: ApiService,
    sharedPreferencesManager: SharedPreferencesManager,
    connectionManager: ConnectionManager
) : BaseViewModel(apiService, sharedPreferencesManager, connectionManager) {

    val success = MutableLiveData<Boolean>()

    fun rateMovie(id: Int, rate: Double) {
        viewModelScope.launch {
            try {
                val data = apiService.postLikeMovieAsync(
                    movie_id = id,
                    guest_session_id = sharedPreferencesManager.guestSessionToken,
                    userData = Rate(rate)
                )
                    .await()

                if (data.isSuccessful) {
                    success.postValue(true)
                } else {
                    success.postValue(false)
                }
            } catch (e: Exception) {
                success.postValue(false)
            }
        }
    }

    data class Rate(
        val value: Double
    )
}