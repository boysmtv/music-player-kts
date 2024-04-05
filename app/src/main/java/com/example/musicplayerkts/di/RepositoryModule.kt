package com.example.musicplayerkts.di

import com.example.musicplayerkts.di.network.Repository
import com.example.musicplayerkts.di.network.RepositoryImpl
import com.example.musicplayerkts.remote.ApiService

fun createRepository(apiService: ApiService): Repository {
    return RepositoryImpl(apiService)
}