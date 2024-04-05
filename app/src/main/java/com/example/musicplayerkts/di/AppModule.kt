package com.example.musicplayerkts.di

import com.example.musicplayerkts.activity.music.vm.MusicViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureStag = module {
    // for get users
    viewModel { MusicViewModel(get()) }
    single { getMusicUC(get()) }

    // create repository
    single { createRepository(get()) }
}
