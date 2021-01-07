package com.stenleone.stanleysfilm.di

import com.stenleone.stanleysfilm.viewModel.network.FilmViewModel
import com.stenleone.stanleysfilm.viewModel.network.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { MainViewModel(get(), get()) }
    viewModel { FilmViewModel(get(), get()) }
}