package com.example.musicplayerkts.di

import com.example.musicplayerkts.activity.music.usecase.MusicUC
import com.example.musicplayerkts.di.network.Repository

fun getMusicUC(repository: Repository): MusicUC {
    return MusicUC(repository)
}