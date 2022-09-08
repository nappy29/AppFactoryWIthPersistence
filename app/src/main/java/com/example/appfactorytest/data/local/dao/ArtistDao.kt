package com.example.appfactorytest.data.local.dao

import androidx.room.*
import com.example.appfactorytest.data.model.Artist

@Dao
abstract class ArtistDao {

    @Transaction
    open suspend fun insertArtist(id: Long, artist: Artist){
        artist.containingAlbumId = id
        insert(artist)
    }

    @Insert
    abstract suspend fun insert(artist: Artist)

//    @Transaction
//    @Insert
//    abstract suspend fun insertAllArtists(artists: List<Artist>)

    @Delete
    abstract suspend fun deleteArtist(artist: Artist)
}