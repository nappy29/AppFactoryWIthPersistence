package com.example.appfactorytest.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.appfactorytest.data.source.ArtistPagingSource
import com.example.appfactorytest.data.local.dao.AlbumDao
import com.example.appfactorytest.data.local.dao.ArtistDao
import com.example.appfactorytest.data.local.dao.TrackDao
import com.example.appfactorytest.data.model.*
import com.example.appfactorytest.data.remote.ApiService
import com.example.appfactorytest.data.source.TopAlbumPagingSource
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: ApiService, private val albumDao: AlbumDao,
        private val trackDao: TrackDao, private val artistDao: ArtistDao): RepositoryHelper {

//    override suspend fun searchByArtist(artistName: String): Response<ArtistSearchApiResponse> {
//        return apiService.searchByArtist(artistName)
//    }

    override suspend fun searchByArtist(
        artistName: String
    ): LiveData<PagingData<Artist>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false,
                initialLoadSize = 2
            ),
            pagingSourceFactory = {
                ArtistPagingSource(apiService, artistName)
            }
            , initialKey = 1
        ).liveData
    }

    override suspend fun searchTopAlbumByArtist(artistName: String): LiveData<PagingData<Album>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false,
                initialLoadSize = 2
            ),
            pagingSourceFactory = {
                TopAlbumPagingSource(apiService, artistName)
            }
            , initialKey = 1
        ).liveData
    }

    override suspend fun isAlbumLocallyStored(album: Album): Boolean {
        return albumDao.isAlbumLocallyStored(album.name, album.url)
    }

    override suspend fun getSavedAlbumByName(album: Album): Album {
        return albumDao.getSavedAlbumByName(album.name, album.url)
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

    override suspend fun getSingleAlbumsWithTracksAndArtist(id: Long): AlbumWithTracks {
        return albumDao.getSingleAlbumsWithTracksAndArtist(id)
    }

    override suspend fun insertAlbum(album: Album): Long {
        return albumDao.insertAlbum(album)
    }

    override suspend fun deleteAlbum(album: Album) {
        return albumDao.deleteAlbum(album)
    }

    override suspend fun insertAllTracks(id: Long, tracks: List<Track>) {
        trackDao.insertAllTracks(id,tracks)
    }

    override suspend fun insertArtist(id: Long, artist: Artist) {
        artistDao.insertArtist(id, artist)
    }
}