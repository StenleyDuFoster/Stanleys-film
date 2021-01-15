package com.stenleone.stanleysfilm.managers.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.stenleone.stanleysfilm.model.entity.FirebaseConfigsEnum
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRemoteConfigManager @Inject constructor() {

    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance().apply {
        val config = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(TimeUnit.HOURS.toSeconds(1))
            .build()
        setConfigSettingsAsync(config)
        setDefaultsAsync(FirebaseConfigsEnum.values().associate { it.key to it.defaultValue })
        fetchAndActivate()
    }

    private val firebaseConfig
        get() = firebaseRemoteConfig.apply { fetchAndActivate() }

    fun getInt(config: FirebaseConfigsEnum) = firebaseConfig.getLong(config.key).toInt()

    fun getString(config: FirebaseConfigsEnum, vararg replace: String): String = firebaseConfig.getString(config.key).let { string ->
        var result = string
        replace.forEach { result = result.replaceFirst("%s", it) }
        return result
    }

    fun getBoolean(config: FirebaseConfigsEnum) = firebaseConfig.getBoolean(config.key)
}