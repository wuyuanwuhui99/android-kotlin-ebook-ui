package com.player.ebook.http

import com.player.ebook.config.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestUtils{

    // 设置 网络请求 Url
    //设置使用Gson解析(记得加入依赖)
    val intanst = Retrofit.Builder()
        .baseUrl(Api.HOST)
        .client(TokenHeaderInterceptor().getClient().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(RequestService::class.java)
}
