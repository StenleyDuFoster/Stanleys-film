package ua.oleksii.fitplantest.model.network.cache

import com.stenleone.stanleysfilm.managers.ConnectionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineCacheInterceptor @Inject constructor(private val connectionManager: ConnectionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        return if (chain.request().method == "GET" && connectionManager.isConnected.value == false) {
            var request = chain.request()
                val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("Pragma")
                    .build()
            return chain.proceed(request)
        } else {
            chain.proceed(chain.request())
        }

    }

}