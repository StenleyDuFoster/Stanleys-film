package ua.oleksii.fitplantest.model.network.cache

import com.stenleone.stanleysfilm.managers.ConnectionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnlineCacheInterceptor @Inject constructor(
    private val connectionManager: ConnectionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        return if (chain.request().method == "GET" && connectionManager.isConnected.value == true) {
            val response = chain.proceed(chain.request())
            val maxAge = 0 // read from cache for x seconds even if there is internet connection
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        } else {
            chain.proceed(chain.request())
        }

    }

}