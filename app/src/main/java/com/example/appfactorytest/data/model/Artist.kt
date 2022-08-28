package com.example.appfactorytest.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "artist", foreignKeys = [
    ForeignKey(
        entity = Album::class,
        parentColumns = arrayOf("album_id"),
        childColumns = arrayOf("containingAlbumId"),
        onDelete = ForeignKey.CASCADE
    )])
data class Artist(

    @PrimaryKey var artist_id:Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("url")
    var url: String,


    @Ignore
    @SerializedName("image")
    var images: List<Image>?,

    @ColumnInfo(name = "image_url")
    var image_url: String,

    var containingAlbumId: Long
){
    constructor() : this(0,"","",null,"",0)
}