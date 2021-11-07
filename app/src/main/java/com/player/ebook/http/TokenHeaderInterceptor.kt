package com.player.ebook.http
import com.player.ebook.common.State
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


//在请求头里添加token的拦截器处理
class TokenHeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = State.token //SpUtils是SharedPreferences的工具类，自行实现
        if (token.isEmpty()) {
            val originalRequest = chain.request()
            return chain.proceed(originalRequest)
        } else {
            val originalRequest = chain.request()
            //key的话以后台给的为准，我这边是叫token
            val updateRequest = originalRequest.newBuilder().header("token", token).build()
            return chain.proceed(updateRequest)
        }
    }


    fun getClient(): OkHttpClient.Builder {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.connectTimeout(15, TimeUnit.SECONDS)
        httpClientBuilder.addNetworkInterceptor(TokenHeaderInterceptor())
        return httpClientBuilder
    }
}
