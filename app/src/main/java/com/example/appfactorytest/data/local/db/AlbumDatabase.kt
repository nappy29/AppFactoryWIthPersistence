package com.example.appfactorytest.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appfactorytest.data.local.dao.AlbumDao
import com.example.appfactorytest.data.local.dao.ArtistDao
import com.example.appfactorytest.data.local.dao.TrackDao
import com.example.appfactorytest.data.model.Album
import com.example.appfactorytest.data.model.AlbumWithTracks
import com.example.appfactorytest.data.model.Artist
import com.example.appfactorytest.data.model.Track

@Database(entities = [Album::class, Track::class, Artist::class], version = 1)
abstract class AlbumDatabase: RoomDatabase() {
    abstract fun getAlbumDao(): AlbumDao
    abstract fun getTrackDao(): TrackDao
    abstract fun getArtistDao(): ArtistDao
}