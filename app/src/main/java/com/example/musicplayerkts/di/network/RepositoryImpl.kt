package com.example.musicplayerkts.di.network

import com.example.musicplayerkts.activity.music.model.MusicModel
import com.example.musicplayerkts.activity.music.model.MusicReqModel
import com.example.musicplayerkts.remote.ApiService

class RepositoryImpl (private val apiService: ApiService) : Repository {

    override suspend fun getMusic(model: MusicReqModel): MusicModel {
        return apiService.getMusic(model.term, model.entity)
    }

}