package com.example.appfactorytest.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class TrackAndArtist(
    @Embedded val track: Track? = null,

//    @Relation(parentColumn = "track_id", entityColumn = "trackOwnerId", entity = Artist::class)
//    val artist: Artist? = null
)