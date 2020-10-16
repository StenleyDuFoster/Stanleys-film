package com.stenleone.stanleysfilm.di

import com.stenleone.stanleysfilm.ui.adapter.recyclerView.HorizontalListRecycler
import org.koin.dsl.module

val adapterModule = module {
    factory { HorizontalListRecycler() }
}

