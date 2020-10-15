package com.stenleone.stanleysfilm.di

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.BASE_URL
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { getListInterceptor(androidContext()) }
    single { getOkkHttp(get(), androidContext()) }
    single { getRetrofit(get()) }
    single { getApiService(get()) }
}

private fun getListInterceptor(context: Context): List<Interceptor> {

    val interceptorDebug = HttpLoggingInterceptor()
    interceptorDebug.level = HttpLoggingInterceptor.Level.BODY
//    val cashInterceptor = CashInterceptor()

    return listOf(
        interceptorDebug,
        ChuckInterceptor(context)
//        cashInterceptor
    )
}

private fun getOkkHttp(listInterceptor: List<Interceptor>, context: Context): OkHttpClient {
    val client = OkHttpClient.Builder()
    val cacheSize = (5 * 1024 * 1024).toLong()
    val myCache = Cache(context.cacheDir, cacheSize)
    client.cache(myCache)

    for (interceptor in listInterceptor) {
        client.addInterceptor(interceptor)
    }

    return client.build()
}

private fun getRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
        )
        .client(client)
        .build()
}

private fun getApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}