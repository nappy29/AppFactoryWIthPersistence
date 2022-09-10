package com.example.appfactorytest.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.appfactorytest.data.model.*
import retrofit2.Response

interface RepositoryHelper {

    suspend fun searchByArtist(artistName: String): LiveData<PagingData<Artist>>

    suspend fun searchTopAlbumByArtist(artistName: String): LiveData<PagingData<Album>>

    suspend fun isAlbumLocallyStored(album: Album): Boolean

    suspend fun getSavedAlbumByName(album: Album): Album

    suspend fun getAlbumTracks(
        artistName: String,
        albumName: String
    ): Response<AlbumAndTracksApiResponse>

    suspend fun getAllAlbumsWithTracksAndArtist(): List<AlbumWithTracks>

    suspend fun getSingleAlbumsWithTracksAndArtist(id: Long): AlbumWithTracks

    suspend fun insertAlbum(album: Album): Long

    suspend fun deleteAlbum(album: Album)

    suspend fun insertAllTracks(id: Long, tracks: List<Track>)

    suspend fun insertArtist(id: Long, artist: Artist)

}