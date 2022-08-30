package com.example.appfactorytest.viewmodel.repository

import com.example.appfactorytest.data.local.dao.AlbumDao
import com.example.appfactorytest.data.local.dao.ArtistDao
import com.example.appfactorytest.data.local.dao.TrackDao
import com.example.appfactorytest.data.model.*
import com.example.appfactorytest.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: ApiService, private val albumDao: AlbumDao,
        private val trackDao: TrackDao, private val artistDao: ArtistDao): RepositoryHelper {

    override suspend fun searchByArtist(artistName: String): Response<ArtistSearchApiResponse> {
        return apiService.searchByArtist(artistName)
    }

    override suspend fun searchTopAlbumByArtist(artistName: String): Response<TopAlbumApiResponse> {
        return apiService.searchTopAlbumByArtist(artistName)
    }

    override suspend fun getAlbumTracks(
        artistName: String,
        albumName: String
    ): Response<AlbumAndTracksApiResponse> {
        return apiService.getAlbumTracks(artistName, albumName)
    }

    override suspend fun getAllAlbumsWithTracksAndArtist(): List<AlbumWithTracks> {
        return albumDao.getAllAlbumsWithTracksAndArtist()
    }

    override suspend fun insertAlbum(album: Album): Long {
        return albumDao.insertAlbum(album)
    }

    override suspend fun deleteAlbum(album: Album) {
        return albumDao.deleteAlbum(album)
    }

    override suspend fun insertAllTracks(tracks: List<Track>) {
        trackDao.insertAllTracks(tracks)
    }

    override suspend fun insertArtist(artist: Artist) {
        artistDao.insertArtist(artist)
    }
}