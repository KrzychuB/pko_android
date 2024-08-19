package pl.kb.movie.network.interceptor

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import pl.kb.movie.utils.Constants

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val mutableHeaders : MutableMap<String, String> = chain.request().headers.toMap().
        toMutableMap()

        if (!mutableHeaders.containsKey("Accept")) {
            mutableHeaders["Accept"] = "application/json"
        }
        if (!mutableHeaders.containsKey("Content-Type")) {
            mutableHeaders["Content-Type"] = "application/json"
        }

        if (!mutableHeaders.containsKey("Authorization")) {
            mutableHeaders["Authorization"] = "Bearer ${Constants.accessToken}"
        }


        val headerBuilder: Headers.Builder = Headers.Builder()
        for ((k, v) in mutableHeaders.entries) {
            headerBuilder.add(k, v)
        }

        val request = chain.request().newBuilder()
        request.headers(headerBuilder.build())
        return chain.proceed(request.build())
    }
}
