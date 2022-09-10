package com.example.appfactorytest.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Transaction
import com.example.appfactorytest.data.model.Track

@Dao
abstract class TrackDao {

    @Insert
    abstract suspend fun insertTrack(track: Track)

    @Transaction
    open suspend fun insertAllTracks(id: Long, tracks: List<Track>) {
        for (track in tracks) {
            track.albumOwnerId = id
            insertTrack(track)
        }
    }

    @Delete
    abstract suspend fun deleteTrack(track: Track)
}