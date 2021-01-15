package com.stenleone.stanleysfilm.util.extencial

import retrofit2.Response

fun <T> Response<T>.successOrError(success: (T) -> Unit, error: (String) -> Unit) {
    if (this.isSuccessful) {
        if (body() != null) {
            body()?.let { success(it) }
        } else {
            error(message())
        }
    } else {
        error(message())
    }
}