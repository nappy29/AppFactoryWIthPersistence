package com.example.appfactorytest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.appfactorytest.data.model.*
import com.example.appfactorytest.util.Resource
import com.example.appfactorytest.data.repository.RepositoryHelper
import com.google.gson.Gson
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repositoryHelper: RepositoryHelper): ViewModel() {

    private val _artistSearchResponse = MutableLiveData<PagingData<Artist>>()

    val artistSearchResponse: LiveData<PagingData<Artist>>
        get() = _artistSearchResponse

    private val _topAlbumSearchResponse = MutableLiveData<Resource<List<Album>>>()

    val topAlbumSearchResponse: LiveData<Resource<List<Album>>>
        get() = _topAlbumSearchResponse

    private val _albumAndTrackSearchResponse = MutableLiveData<Resource<List<Track>>>()

    val albumAndTrackSearchResponse: LiveData<Resource<List<Track>>>
        get() = _albumAndTrackSearchResponse

    private val _albumAndTrackObject = MutableLiveData<AlbumWithTracks>()

    val albumAndTrackObject: LiveData<AlbumWithTracks>
        get() = _albumAndTrackObject

    private val _singleArtistObject = MutableLiveData<Artist>()

    val singleArtistObject: LiveData<Artist>
        get() = _singleArtistObject

    private val _localAlbumTracks = MutableLiveData<List<Album>>()

    val localAlbumTracks: LiveData<List<Album>>
        get() = _localAlbumTracks

    var currentArtistName: String? = null
    var currentAlbum: Album? = null
    private var currentArtistSearchResult: LiveData<PagingData<Artist>>? = null
    private var currentTopAlbumSearchResult: LiveData<PagingData<Album>>? = null

    suspend fun searchByArtist(artistName: String): LiveData<PagingData<Artist>> {

        val lastResult = currentArtistSearchResult
        if (artistName == currentArtistName && lastResult != null) {
            return lastResult
        }
        currentArtistName = artistName

        val response = repositoryHelper.searchByArtist(artistName).cachedIn(viewModelScope)
        currentArtistSearchResult = response
        return response
    }


//    fun topAlbumSearch(artistName: String){
//        viewModelScope.launch {
//            var response = repositoryHelper.searchTopAlbumByArtist(artistName)
//            if(response.isSuccessful) {
//                _topAlbumSearchResponse.postValue(Resource.success(response.body()?.topAlbum?.albums))
//                Log.d("MainViewModel_TopAlbum", response.toString())
//            }else
//                _topAlbumSearchResponse.postValue(Resource.error("Failed to retrieve data", null))
//        }
//    }

   suspend fun topAlbumSearch(artistName: String):LiveData<PagingData<Album>>{
       val lastResult = currentTopAlbumSearchResult
       if (artistName == currentArtistName && lastResult != null) {
           return lastResult
       }
       currentArtistName = artistName

        val response = repositoryHelper.searchTopAlbumByArtist(artistName).cachedIn(viewModelScope)
       currentTopAlbumSearchResult = response

        return response
    }

    fun getAlbumTracks(artistName: String, album: Album){
        viewModelScope.launch(Dispatchers.IO) {
            var response = repositoryHelper.getAlbumTracks(artistName, album.name)
            if(response.isSuccessful) {
                var albumResponseObject = response.body()?.album
                var obj = albumResponseObject?.trackObject?.track
                var tracks = getGsonArrayorObject(obj)

                _albumAndTrackSearchResponse.postValue(Resource.success(tracks))

                var new_album = buildAlbumObject(albumResponseObject!!, album)

                if(!repositoryHelper.isAlbumLocallyStored(new_album)){
                    var album_id = repositoryHelper.insertAlbum(new_album)
                    repositoryHelper.insertArtist(album_id,album.artist!!)
                    tracks?.let { repositoryHelper.insertAllTracks(album_id, it) }

                    getAlbumWithTracksById(album_id)
                }else{
                    var album = repositoryHelper.getSavedAlbumByName(new_album)
                    getAlbumWithTracksById(album.album_id)
                }


//                var albumWithTracks = repositoryHelper.getSingleAlbumsWithTracksAndArtist(album_id)
////                var albumWithTracks = builAlbumWithTracks(new_album, albumResponseObject)
//
//                Log.d("MainVM", albumWithTracks.toString())
//                setlbumithTracksObject(albumWithTracks)

            }else
                _albumAndTrackSearchResponse.postValue(Resource.error("Failed to retrieve data", null))
        }
    }


    fun getAllAlbumTracksLocally(){
        viewModelScope.launch {
            var albumWithTracks = repositoryHelper.getAllAlbumsWithTracksAndArtist()

            var list = mutableListOf<Album>()

            for(obj in albumWithTracks)
                list.add(obj.album)

            Log.e("viewmode", list.toString())
            _localAlbumTracks.postValue(list)
        }
    }

    fun getGsonArrayorObject(trackElement: JsonElement?): List<Track>?{
        var gson = Gson()

        var tracks = mutableListOf<Track>()

        if (trackElement?.isJsonObject == true) {
            var track: Track = gson?.fromJson(trackElement.toString(), Track::class.java)
            tracks.add(track)
            return tracks
        }
        else if (trackElement?.isJsonArray == true) {
            return gson.fromJson(trackElement.toString(), Array<Track>::class.java).toList()
        }

        return null
    }

    fun getAlbumWithTracksById(id: Long) {

        viewModelScope.launch {
            var albumWithTracks = repositoryHelper.getSingleAlbumsWithTracksAndArtist(id)
            setalbumithTracksObject(albumWithTracks)
            Log.e("getAlbumWithTracksById", albumWithTracks.toString())
        }
    }


    fun buildAlbumObject(albumResponseObject: AlbumResponse, album: Album): Album{
        var _album = Album()
        var artist = Artist()

        _album.name = album.name
        _album.url = albumResponseObject.url
        _album.image_url = albumResponseObject.images[1].url

        _album.artist = album.artist

        return _album
    }

//    fun builAlbumWithTracks(album: Album, albumResponseObject: AlbumResponse): AlbumWithTracks{
//        var obj = AlbumWithTracks(album, album.artist, albumResponseObject.trackObject?.tracks)
//
//        return obj
//    }

    fun setArtistObject(artist: Artist){
        _singleArtistObject.postValue(artist)
    }

    fun setalbumithTracksObject(albumWithTracks: AlbumWithTracks){
        _albumAndTrackObject.postValue(albumWithTracks)
    }

}