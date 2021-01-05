package com.stenleone.stanleysfilm.di

import com.stenleone.stanleysfilm.managers.SharedPreferencesManager
import com.stenleone.stanleysfilm.managers.SharedPreferencesSortMainManager
import org.koin.dsl.module

val managerModule = module {
    single { SharedPreferencesManager(get()) }
    single { SharedPreferencesSortMainManager(get()) }
}