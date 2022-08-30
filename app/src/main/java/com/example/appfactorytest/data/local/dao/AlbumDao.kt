package com.example.appfactorytest.data.local.dao

import androidx.room.*
import com.example.appfactorytest.data.model.Album
import com.example.appfactorytest.data.model.AlbumWithTracks

@Dao
interface AlbumDao {

    @Transaction
    @Query("SELECT * FROM album")
    suspend fun getAllAlbumsWithTracksAndArtist(): List<AlbumWithTracks>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAlbums(albums: List<Album>)

    @Transaction
    @Delete
    suspend fun deleteAlbum(album: Album)
}