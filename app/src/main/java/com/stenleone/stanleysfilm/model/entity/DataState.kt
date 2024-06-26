package com.stenleone.stanleysfilm.model.entity

sealed class DataState<out R> {
    data class Success<out T>(val data: T): DataState<T>()
    data class Error(val exception: RequestError): DataState<Nothing>()
}