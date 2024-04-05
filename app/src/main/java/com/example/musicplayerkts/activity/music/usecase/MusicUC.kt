package com.example.musicplayerkts.activity.music.usecase

import com.example.musicplayerkts.activity.music.model.MusicModel
import com.example.musicplayerkts.activity.music.model.MusicReqModel
import com.example.musicplayerkts.di.network.Repository
import com.example.musicplayerkts.domain.usecase.UseCase

class MusicUC(
    private val repository: Repository
) : UseCase<MusicModel, Any?>() {

    private val TAG = this::class.java.simpleName

    override suspend fun run(params: Any?): MusicModel {
        return repository.getMusic(params as MusicReqModel)
    }

}