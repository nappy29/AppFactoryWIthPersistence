package com.example.appfactorytest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfactorytest.data.model.*
import com.example.appfactorytest.util.Resource
import com.example.appfactorytest.viewmodel.repository.RepositoryHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repositoryHelper: RepositoryHelper): ViewModel() {

    private val _artistSearchResponse = MutableLiveData<Resource<List<Artist>>>()

    val artistSearchResponse: LiveData<Resource<List<Artist>>>
        get() = _artistSearchResponse

    private val _topAlbumSearchResponse = MutableLiveData<Resource<List<Album>>>()

    val topAlbumSearchResponse: LiveData<Resource<List<Album>>>
        get() = _topAlbumSearchResponse

    private val _albumAndTrackSearchResponse = MutableLiveData<Resource<List<Track>>>()

    val albumAndTrackSearchResponse: LiveData<Resource<List<Track>>>
        get() = _albumAndTrackSearchResponse

    private val _singleAlbumObject = MutableLiveData<AlbumWithTracks>()

    val singleAlbumObject: LiveData<AlbumWithTracks>
        get() = _singleAlbumObject


    fun searchByArtist(artistName: String){
        viewModelScope.launch {
            var artistSearchApiResponse = repositoryHelper.searchByArtist(artistName)
            if(artistSearchApiResponse.isSuccessful) {
                _artistSearchResponse.postValue(Resource.success(artistSearchApiResponse.body()?.results?.artistMatches?.artists))
                Log.d("MainViewModel_Artist", artistSearchApiResponse.body()?.results.toString())
            }else
                _artistSearchResponse.postValue(Resource.error("Failed to retrieve data", null))
        }
    }

    fun topAlbumSearch(artistName: String){
        viewModelScope.launch {
            var response = repositoryHelper.searchTopAlbumByArtist(artistName)
            if(response.isSuccessful) {
                _topAlbumSearchResponse.postValue(Resource.success(response.body()?.topAlbum?.albums))
                Log.d("MainViewModel_TopAlbum", response.toString())
            }else
                _topAlbumSearchResponse.postValue(Resource.error("Failed to retrieve data", null))
        }
    }

    fun getAlbumTracks(artistName: String, album: Album){
        viewModelScope.launch {
            var response = repositoryHelper.getAlbumTracks(artistName, album.name)
            if(response.isSuccessful) {
                _albumAndTrackSearchResponse.postValue(Resource.success(response.body()?.album?.trackObject?.tracks))

                var albumResponseObject = response.body()?.album
                var new_album = buildAlbumObject(albumResponseObject!!, album)

                var album_id = repositoryHelper.insertAlbum(new_album)
                var artist_id = repositoryHelper.insertArtist(album.artist!!)
                repositoryHelper.insertAllTracks(albumResponseObject.trackObject.tracks)

                Log.d("MainViewModel_TopAlbum", response.toString())
            }else
                _albumAndTrackSearchResponse.postValue(Resource.error("Failed to retrieve data", null))
        }
    }

    fun buildAlbumObject(albumResponseObject: AlbumResponse, album: Album): Album{
        var _album = Album()
        var artist = Artist()

        _album.name = album.name
        _album.url = albumResponseObject.url
        _album.image_url = albumResponseObject.images[1].toString()

        _album.artist = album.artist

        return _album
    }

    fun setAlbumObject(album: AlbumWithTracks){
        _singleAlbumObject.postValue(album)
    }

}