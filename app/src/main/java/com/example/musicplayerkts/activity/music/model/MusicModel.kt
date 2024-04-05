package com.example.musicplayerkts.activity.music.model

import com.google.gson.annotations.SerializedName

data class MusicModel(

    @SerializedName("resultCount")
    var resultCount: Int? = null,

    @SerializedName("results")
    var results: List<MusicResultModel>? = null,

)

data class MusicResultModel (

    @SerializedName("artistId")
    var artistId:Long?= null,

    @SerializedName("collectionId")
    var collectionId:Long? = null,

    @SerializedName("trackId")
    var trackId:Long? = null,

    @SerializedName("artistName")
    var artistName: String? = null,

    @SerializedName("collectionName")
    var collectionName: String? = null,

    @SerializedName("trackName")
    var trackName: String? = null,

    @SerializedName("previewUrl")
    var previewUrl: String? = null,

    @SerializedName("artworkUrl100")
    var artworkUrl100: String? = null,

    @SerializedName("isPlay")
    var isPlay: Boolean = false,

)

data class MusicReqModel(

    @SerializedName("term")
    var term: String? = null,

    @SerializedName("entity")
    var entity: String = "song",

)