package com.stenleone.stanleysfilm.di

import com.stenleone.stanleysfilm.ui.adapter.recyclerView.GenreListRecycler
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.HorizontalListMovie
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.StudiosListRecycler
import org.koin.dsl.module

val adapterModule = module {
    factory { HorizontalListMovie() }
    factory { GenreListRecycler() }
    factory { StudiosListRecycler() }
}