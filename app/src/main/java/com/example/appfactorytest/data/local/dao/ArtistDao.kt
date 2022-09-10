package com.example.appfactorytest.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Transaction
import com.example.appfactorytest.data.model.Artist

@Dao
abstract class ArtistDao {

    @Transaction
    open suspend fun insertArtist(id: Long, artist: Artist) {
        artist.containingAlbumId = id
        insert(artist)
    }

    @Insert
    abstract suspend fun insert(artist: Artist)

    @Delete
    abstract suspend fun deleteArtist(artist: Artist)
}