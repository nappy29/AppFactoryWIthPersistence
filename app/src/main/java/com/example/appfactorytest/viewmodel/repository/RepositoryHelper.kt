package com.example.appfactorytest.viewmodel.repository

import com.example.appfactorytest.data.model.*
import retrofit2.Response

interface RepositoryHelper {

    suspend fun searchByArtist(artistName: String): Response<ArtistSearchApiResponse>

    suspend fun searchTopAlbumByArtist(artistName: String): Response<TopAlbumApiResponse>

    suspend fun getAlbumTracks(artistName: String, albumName: String): Response<AlbumAndTracksApiResponse>

    suspend fun getAllAlbumsWithTracksAndArtist(): List<AlbumWithTracks>

    suspend fun insertAlbum(album: Album): Long

    suspend fun deleteAlbum(album: Album)

    suspend fun insertAllTracks(tracks: List<Track>)

    suspend fun insertArtist(artist: Artist)

}