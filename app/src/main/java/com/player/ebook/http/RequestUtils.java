package com.player.ebook.http;

import com.player.ebook.config.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RequestUtils {

    private static final RequestService requestService =  new Retrofit.Builder()
            .baseUrl(API.HOST) // 设置 网络请求 Url
            .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
            .build().create(RequestService.class);

    public static RequestService  getIntanst(){
        return requestService;
    }
}
