package com.viseo.mybookshelf.data.di

import android.content.Context
import com.example.data.BuildConfig
import com.viseo.mybookshelf.data.database.AppDatabase
import com.viseo.mybookshelf.data.database.dao.BookDao
import com.viseo.mybookshelf.data.network.BookSearchAPI
import com.viseo.mybookshelf.data.network.CacheInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache =
        Cache(
            File(context.cacheDir, "http-cache"),
            10L * 1024L * 1024L
        )

    @Singleton
    @Provides
    fun provideOkHttpClient(cache: Cache): OkHttpClient =
        OkHttpClient
            .Builder()
            .cache(cache)
            .addNetworkInterceptor(CacheInterceptor())
            .build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BOOK_SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


    @Provides
    @Singleton
    fun provideBookSearchApi(retrofit: Retrofit): BookSearchAPI =
        retrofit.create(BookSearchAPI::class.java)

    @Singleton
    @Provides
    fun provideBookDao(@ApplicationContext context: Context): BookDao =
        AppDatabase.getDatabase(context).bookDao()
}