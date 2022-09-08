package com.example.appfactorytest.di.module

import android.content.Context
import androidx.room.Room
import com.example.appfactorytest.BuildConfig
import com.example.appfactorytest.data.local.dao.AlbumDao
import com.example.appfactorytest.data.local.dao.ArtistDao
import com.example.appfactorytest.data.local.dao.TrackDao
import com.example.appfactorytest.data.local.db.AlbumDatabase
import com.example.appfactorytest.data.remote.ApiService
import com.example.appfactorytest.util.NetworkHelper
import com.example.appfactorytest.data.repository.RepositoryHelper
import com.example.appfactorytest.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AlbumDatabase {
        return Room.databaseBuilder(
            appContext,
            AlbumDatabase::class.java,
            "RssReader"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAlbumDao(appDatabase: AlbumDatabase): AlbumDao {
        return appDatabase.getAlbumDao()
    }

    @Provides
    @Singleton
    fun provideTrackDao(appDatabase: AlbumDatabase): TrackDao {
        return appDatabase.getTrackDao()
    }

    @Provides
    @Singleton
    fun provideArtistDao(appDatabase: AlbumDatabase): ArtistDao {
        return appDatabase.getArtistDao()
    }

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {

        val apiInterceptor = Interceptor { chain ->

            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.APIKEY)
                .build()

            val reqBuilder = original.newBuilder()
                .url(url)
            chain.proceed(reqBuilder.build())
        }

        return OkHttpClient
            .Builder()
            .addInterceptor(apiInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }



    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideRepository(api: ApiService, albumDao: AlbumDao, trackDao: TrackDao, artistDao: ArtistDao):
            RepositoryHelper = RepositoryImpl(api, albumDao, trackDao, artistDao)

    @Provides
    @Singleton
    fun provideNetworkHelper(@ApplicationContext appContext: Context): NetworkHelper = NetworkHelper(appContext)
}