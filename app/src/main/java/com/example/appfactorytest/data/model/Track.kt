package com.example.appfactorytest.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "track", foreignKeys = [
    ForeignKey(
        entity = Album::class,
        parentColumns = arrayOf("album_id"),
        childColumns = arrayOf("albumOwnerId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Track(
    @PrimaryKey(autoGenerate = true) var track_id:Int,

    @SerializedName("duration")
    var duration: Long,

    @SerializedName("url")
    var url: String,

    @SerializedName("name")
    var name: String,

//    @Embedded(prefix = "artist_")
//    @Ignore
//    @SerializedName("artist")
//    var artist: Artist,

    var albumOwnerId: Long
){
    constructor() : this(0,0,"","", 0)
}