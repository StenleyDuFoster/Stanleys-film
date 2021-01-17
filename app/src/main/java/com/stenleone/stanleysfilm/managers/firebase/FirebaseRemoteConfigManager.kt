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

    fun getInt(config: FirebaseConfigsEnum) = firebaseRemoteConfig.getLong(config.key).toInt()

    fun getIntAsync(config: FirebaseConfigsEnum,  success: (Int) -> Unit, failure: (String) -> Unit = {}) {

        firebaseRemoteConfig.fetchAndActivate()
            .addOnSuccessListener {
                firebaseRemoteConfig.getLong(config.key).let { long ->
                    success(long.toInt())
                }
            }
            .addOnFailureListener {
                failure(it.message.toString())
            }
    }

    fun getString(config: FirebaseConfigsEnum, vararg replace: String): String = firebaseRemoteConfig.getString(config.key).let { string ->
        var result = string
        replace.forEach { result = result.replaceFirst("%s", it) }
        return result
    }

    fun getStringAsync(config: FirebaseConfigsEnum,  success: (String) -> Unit, failure: (String) -> Unit, vararg replace: String) {

        firebaseRemoteConfig.fetchAndActivate()
            .addOnSuccessListener {
                firebaseRemoteConfig.getString(config.key).let { string ->
                    var result = string
                    replace.forEach { result = result.replaceFirst("%s", it) }
                    success(result)
                }
            }
            .addOnFailureListener {
                failure(it.message.toString())
            }
    }

    fun getBoolean(config: FirebaseConfigsEnum) = firebaseRemoteConfig.getBoolean(config.key)
}