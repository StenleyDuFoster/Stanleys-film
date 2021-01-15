package com.stenleone.stanleysfilm.model.entity

import com.stenleone.stanleysfilm.BuildConfig

enum class FirebaseConfigsEnum(val key: String, val defaultValue: Any) {
    APP_VERSION_CODE("app_version_code", BuildConfig.VERSION_CODE)
}