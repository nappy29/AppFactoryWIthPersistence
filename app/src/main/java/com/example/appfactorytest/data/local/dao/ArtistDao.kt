package com.example.appfactorytest.data.local.dao

import androidx.room.*
import com.example.appfactorytest.data.model.Artist

@Dao
interface ArtistDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artist: Artist)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(artists: List<Artist>)

    @Transaction
    @Delete
    suspend fun deleteTrack(artist: Artist)
}