package com.stenleone.stanleysfilm.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RequestError(
    var type: String,
    var code: Int? = null,
    var title: String? = null,
    var message: String? = null
) : Parcelable {
    companion object {
        const val CONNECTION_ERROR = "CONNECTION_ERROR"
        const val REQUEST_ERROR = "REQUEST_ERROR"
        const val UNSUCCESS_STATUS = "REQUEST_ERROR"
    }
}