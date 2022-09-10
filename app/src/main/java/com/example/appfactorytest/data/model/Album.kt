package com.example.appfactorytest.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "album")
data class Album(
    @PrimaryKey(autoGenerate = true) var album_id: Long,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name: String,

    @Ignore
    @SerializedName("artist")
    var artist: Artist?,

    @ColumnInfo(name = "url")
    @SerializedName("url")
    var url: String,

    @Ignore
    @SerializedName("image")
    var imageList: List<Image>?,

    var image_url: String,

    var isLocal: Boolean
) {
    constructor() : this(0, "", null, "", null, "", false)
}
