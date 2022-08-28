package com.example.appfactorytest.data.model

import com.google.gson.annotations.SerializedName

data class TopAlbumApiResponse(

    @SerializedName("topalbums")
    var topAlbum: TopAlbum
)

data class TopAlbum(

    @SerializedName("album")
    var albums: List<Album>
)