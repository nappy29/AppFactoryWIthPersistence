package com.example.appfactorytest.data.local.dao

import androidx.room.*
import com.example.appfactorytest.data.model.Album
import com.example.appfactorytest.data.model.AlbumWithTracks

@Dao
abstract class AlbumDao {

    @Transaction
    @Query("SELECT * FROM album")
    abstract suspend fun getAllAlbumsWithTracksAndArtist(): List<AlbumWithTracks>

    @Query("SELECT * FROM album WHERE album_id =:id")
    abstract suspend fun getSingleAlbumsWithTracksAndArtist(id: Long): AlbumWithTracks

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAlbum(album: Album): Long

    @Query("SELECT EXISTS (SELECT 1 FROM album WHERE name = :albumName AND url=:url)")
    abstract suspend fun isAlbumLocallyStored(albumName: String, url: String): Boolean

    @Query("SELECT * FROM album WhERE name=:albumName AND url=:url")
    abstract suspend fun getSavedAlbumByName(albumName: String, url: String): Album

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllAlbums(albums: List<Album>)

    @Delete
    abstract suspend fun deleteAlbum(album: Album)
}