package com.example.appfactorytest.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class AlbumWithTracks(
    @Embedded val album: Album,

    @Relation(parentColumn = "album_id", entityColumn = "containingAlbumId", entity = Artist::class)
    val artist: Artist? = null,

    @Relation(parentColumn = "album_id", entityColumn = "albumOwnerId", entity = Track::class)
    val tracks: List<Track>? = null
)