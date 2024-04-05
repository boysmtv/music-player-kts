package com.example.musicplayerkts.di.network

import com.example.musicplayerkts.activity.music.model.MusicModel
import com.example.musicplayerkts.activity.music.model.MusicReqModel

interface Repository {

    suspend fun getMusic(model: MusicReqModel): MusicModel

}