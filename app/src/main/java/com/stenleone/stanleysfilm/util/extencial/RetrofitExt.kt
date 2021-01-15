package com.stenleone.stanleysfilm.util.extencial

import retrofit2.Response

fun <T> Response<T>.successOrError(success: (T) -> Unit, error: (String) -> Unit) {
    if (this.isSuccessful) {
        val body = body()
        if (body != null) {
            success(body)
        } else {
            error(message())
        }
    } else {
        error(message())
    }
}