package com.example.appfactorytest.data.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.appfactorytest.data.local.dao.AlbumDao
import com.example.appfactorytest.data.model.Album
import com.example.appfactorytest.data.remote.ApiService

class TopAlbumPagingSource(
    private val apiService: ApiService,
    private val albumDao: AlbumDao,
    private val artistName: String
) :
    PagingSource<Int, Album>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {

        return try {
            val position = params.key ?: 1
            val response = apiService.searchTopAlbumByArtist(artistName, position)
            Log.e("MainVii", response.toString())

            var list = response.body()!!.topAlbum.albums

            for (album in list) {
                album.isLocal = albumDao.isAlbumLocallyStored(album.name, album.url)
            }
            LoadResult.Page(
                data = list,
                prevKey = if (position == 1) null else position - 1,
                nextKey = position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Album>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}