package com.example.appfactorytest.data.model

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class AlbumAndTracksApiResponse(
    @SerializedName("album")
    val album: AlbumResponse
)

data class AlbumResponse(
    @SerializedName("artist")
    var artistName: String,

    @SerializedName("url")
    var url: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("listeners")
    var numOfListeners: Long,

    @SerializedName("image")
    var images: List<Image>,

    @SerializedName("tracks")
    var trackObject: TrackObject?
)

data class TrackObject(

    @SerializedName("track")
    var track: JsonElement?
)