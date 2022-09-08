package com.example.appfactorytest.data.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.appfactorytest.data.model.Artist
import com.example.appfactorytest.data.remote.ApiService

class ArtistPagingSource(private val apiService: ApiService, private val artistName: String): PagingSource<Int, Artist>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {

        return try {
            val position = params.key ?: 1
            val response = apiService.searchByArtist(artistName,position)
            Log.e("MainVii", response.toString())
            LoadResult.Page(data = response.body()!!.results.artistMatches.artists, prevKey = if (position == 1) null else position - 1,
                nextKey = position + 1)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}