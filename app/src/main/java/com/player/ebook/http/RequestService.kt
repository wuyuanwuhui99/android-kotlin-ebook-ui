package com.player.ebook.http

import com.player.ebook.config.API
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RequestService {
    @GET(API.GETUSERDATA)
    fun getUserData(@Header("Authorization")token:String): Call<ResultEntity>

    @GET(API.FINDALLBYCLASSIFYGROUP)
    fun getAllClassify(@Header("Authorization")token:String): Call<ResultEntity>

    @GET(API.FINDBOOLIST)
    fun findBootList(
        @Header("Authorization")token:String,
        @Query("classify") classify:String,
        @Query("pageNum") pageNum:Int,
        @Query("pageSize") pageSize:Int
    ): Call<ResultEntity>
}