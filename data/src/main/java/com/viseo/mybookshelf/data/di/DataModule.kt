package com.viseo.mybookshelf.data.di

import android.content.Context
import android.util.Log
import com.viseo.mybookshelf.data.database.AppDatabase
import com.viseo.mybookshelf.data.database.dao.BookDao
import com.viseo.mybookshelf.data.network.BookSearchAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext applicationContext: Context): OkHttpClient =
        OkHttpClient
            .Builder()
            .cache(
                Cache(
                    File(applicationContext.cacheDir, "http-cache"),
                    10L * 1024L * 1024L
                )
            ) // 10 MiB
            .addNetworkInterceptor(CacheInterceptor())
            .build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://openlibrary.org/")
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

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("RetrofitURL", chain.request().url().toString())
        val response: Response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(10, TimeUnit.DAYS)
            .build()
        return response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}