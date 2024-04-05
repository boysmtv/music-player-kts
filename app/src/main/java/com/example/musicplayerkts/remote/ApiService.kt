package com.example.musicplayerkts.remote

import com.example.musicplayerkts.activity.music.model.MusicModel
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("Content-Type: application/json")
    @GET("search")
    suspend fun getMusic(
        @Query("term") query: String?,
        @Query("entity") sort: String?
    ) : MusicModel

}