package com.example.musicplayerkts.activity.music.model

import com.google.gson.annotations.SerializedName

data class SongModel(

    @SerializedName("title")
    var title:String?= null,

    @SerializedName("authorName")
    var authorName:String? = null,

    @SerializedName("songUrl")
    var songUrl:String? = null,

    @SerializedName("isPlay")
    var isPlay: Boolean = false,

)