package com.stenleone.stanleysfilm.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.stenleone.stanleysfilm.BuildConfig
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.oleksii.fitplantest.model.network.cache.OfflineCacheInterceptor
import ua.oleksii.fitplantest.model.network.cache.OnlineCacheInterceptor
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        private const val GENERAL_TIMEOUT = 25L
        private const val CONNECT_TIMEOUT = GENERAL_TIMEOUT
        private const val WRITE_TIMEOUT = GENERAL_TIMEOUT
        private const val READ_TIMEOUT = GENERAL_TIMEOUT
        private const val CACHE_SIZE = 20L * 1024 * 1024
    }

    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache = Cache(context.cacheDir, CACHE_SIZE)

    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        cache: Cache,
        onlineCacheInterceptor: OnlineCacheInterceptor,
        offlineCacheInterceptor: OfflineCacheInterceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(offlineCacheInterceptor)
            .addNetworkInterceptor(onlineCacheInterceptor)
            .cache(cache)

        if (BuildConfig.DEBUG) {
            client.addNetworkInterceptor(ChuckInterceptor(context))
        }

        return client.build()
    }


    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(TmdbNetworkConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

}