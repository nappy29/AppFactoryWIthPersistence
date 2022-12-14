package com.example.appfactorytest.data.remote

import com.example.appfactorytest.data.model.AlbumAndTracksApiResponse
import com.example.appfactorytest.data.model.ArtistSearchApiResponse
import com.example.appfactorytest.data.model.TopAlbumApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("?method=artist.search&format=json")
    suspend fun searchByArtist(
        @Query("artist", encoded = true) artistName: String,
        @Query("page", encoded = true) page: Int
    ): Response<ArtistSearchApiResponse>


    @GET("?method=artist.gettopalbums&format=json")
    suspend fun searchTopAlbumByArtist(
        @Query("artist", encoded = true) artistName: String,
        @Query("page", encoded = true) page: Int
    ): Response<TopAlbumApiResponse>


    @GET("?method=album.getinfo&format=json")
    suspend fun getAlbumTracks(
        @Query("artist", encoded = true) artistName: String,
        @Query("album", encoded = true) albumName: String
    ): Response<AlbumAndTracksApiResponse>

}