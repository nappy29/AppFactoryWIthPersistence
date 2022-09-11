package com.example.appfactorytest.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.appfactorytest.data.model.*
import com.example.appfactorytest.data.repository.RepositoryHelper
import com.example.appfactorytest.util.NetworkStateManager
import com.example.appfactorytest.util.Resource
import com.google.gson.Gson
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repositoryHelper: RepositoryHelper) :
    ViewModel() {

    val _connectivity_res = NetworkStateManager.getNetworkConnectivityStatus() as LiveData

    private val _albumAndTrackSearchResponse = MutableLiveData<Resource<List<Track>>>()

    val albumAndTrackSearchResponse: LiveData<Resource<List<Track>>>
        get() = _albumAndTrackSearchResponse

    private val _albumAndTrackObject = MutableLiveData<AlbumWithTracks?>()

    val albumAndTrackObject: MutableLiveData<AlbumWithTracks?>
        get() = _albumAndTrackObject

    private val _singleArtistObject = MutableLiveData<Artist>()

    val singleArtistObject: LiveData<Artist>
        get() = _singleArtistObject

    private val _localAlbumTracks = MutableLiveData<List<Album>>()

    val localAlbumTracks: LiveData<List<Album>>
        get() = _localAlbumTracks

    var currentArtistName: String? = null

    private val _statusLiveData = MutableLiveData<Resource<Any>>()

    val statusLiveData: LiveData<Resource<Any>>
        get() = _statusLiveData
    private var currentArtistSearchResult: LiveData<PagingData<Artist>>? = null
    private var currentTopAlbumSearchResult: LiveData<PagingData<Album>>? = null

    suspend fun searchByArtist(artistName: String): LiveData<PagingData<Artist>> {

        _statusLiveData.postValue(Resource.success(null))
        val lastResult = currentArtistSearchResult
        if (artistName == currentArtistName && lastResult != null) {
            return lastResult
        }
        _statusLiveData.postValue(Resource.loading(null))
        currentArtistName = artistName

        val response = repositoryHelper.searchByArtist(artistName).cachedIn(viewModelScope)
        currentArtistSearchResult = response

        if (response == null)
            _statusLiveData.postValue(
                Resource.error(
                    "An error occured. Make sure you have an active connection",
                    null
                )
            )
        return response
    }

    suspend fun topAlbumSearch(artistName: String): LiveData<PagingData<Album>>? {
        _statusLiveData.postValue(Resource.success(null))

        try {

            val lastResult = currentTopAlbumSearchResult
            if (artistName == currentArtistName && lastResult != null) {
                return lastResult
            }
            _statusLiveData.postValue(Resource.success(null))
            currentArtistName = artistName

            val response =
                repositoryHelper.searchTopAlbumByArtist(artistName).cachedIn(viewModelScope)

            if (response == null)
                _statusLiveData.postValue(
                    Resource.error(
                        "An error occured. Make sure you have an active connection",
                        null
                    )
                )
            currentTopAlbumSearchResult = response

            return response

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getAlbumTracks(artistName: String, album: Album) {
        _albumAndTrackSearchResponse.postValue(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {

            try {
                var response = repositoryHelper.getAlbumTracks(artistName, album.name)
                if (response.isSuccessful) {
                    var albumResponseObject = response.body()?.album
                    var obj = albumResponseObject?.trackObject?.track
                    var tracks = getGsonArrayorObject(obj)

                    _albumAndTrackSearchResponse.postValue(Resource.success(tracks))

                    var new_album = buildAlbumObject(albumResponseObject!!, album)

                    if (!repositoryHelper.isAlbumLocallyStored(new_album)) {
                        new_album.isLocal = true
                        var album_id = repositoryHelper.insertAlbum(new_album)
                        repositoryHelper.insertArtist(album_id, album.artist!!)
                        tracks?.let { repositoryHelper.insertAllTracks(album_id, it) }

                        getAlbumWithTracksById(album_id)
                    } else {
                        var album = repositoryHelper.getSavedAlbumByName(new_album)
                        getAlbumWithTracksById(album.album_id)
                    }

                } else
                    _albumAndTrackSearchResponse.postValue(
                        Resource.error(
                            "Failed to retrieve data",
                            null
                        )
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


    fun getAllAlbumTracksLocally() {
        viewModelScope.launch {
            var albumWithTracks = repositoryHelper.getAllAlbumsWithTracksAndArtist()

            var list = mutableListOf<Album>()

            for (obj in albumWithTracks)
                list.add(obj.album)

            _localAlbumTracks.postValue(list)
        }
    }

    fun getGsonArrayorObject(trackElement: JsonElement?): List<Track>? {
        var gson = Gson()

        var tracks = mutableListOf<Track>()

        if (trackElement?.isJsonObject == true) {
            var track: Track = gson?.fromJson(trackElement.toString(), Track::class.java)
            tracks.add(track)
            return tracks
        } else if (trackElement?.isJsonArray == true) {
            return gson.fromJson(trackElement.toString(), Array<Track>::class.java).toList()
        }

        return null
    }

    fun getAlbumWithTracksById(id: Long) {

        viewModelScope.launch {
            var albumWithTracks = repositoryHelper.getSingleAlbumsWithTracksAndArtist(id)
            setalbumithTracksObject(albumWithTracks)
        }
    }


    fun buildAlbumObject(albumResponseObject: AlbumResponse, album: Album): Album {
        var _album = Album()
        _album.name = album.name
        _album.url = albumResponseObject.url
        _album.image_url = albumResponseObject.images[2].url

        _album.artist = album.artist

        return _album
    }

    fun setArtistObject(artist: Artist) {
        _singleArtistObject.postValue(artist)
    }

    fun setalbumithTracksObject(albumWithTracks: AlbumWithTracks?) {
        _statusLiveData.postValue(Resource.loading(null))
        _albumAndTrackObject.postValue(albumWithTracks)
        _statusLiveData.postValue(Resource.success(null))
    }

    fun deleteAlbum(album: Album) {
        viewModelScope.launch {
            repositoryHelper.deleteAlbum(album)
            getAllAlbumTracksLocally()
        }
    }

}