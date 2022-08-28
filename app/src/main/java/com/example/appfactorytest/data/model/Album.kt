package com.example.appfactorytest.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "album")
data class Album(
    @PrimaryKey var album_id:Int,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name: String,

//    @Embedded(prefix = "artist_")
    @Ignore
    @SerializedName("artist")
    var artist: Artist?,

    @ColumnInfo(name = "url")
    @SerializedName("url")
    var url: String,

    @Ignore
    @SerializedName("image")
    var imageList: List<Image>?,

    var image_url: String
 ){
    constructor() : this(0,"",null,"",null,"")
}
