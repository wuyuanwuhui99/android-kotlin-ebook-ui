package com.player.ebook.http

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import com.player.ebook.config.Api

interface RequestService {
    @GET(Api.GETUSERDATA)
    fun getUserData(@Header("Authorization")token:String): Call<ResultEntity>

    @GET(Api.FINDALLBYCLASSIFYGROUP)
    fun getAllClassify(@Header("Authorization")token:String): Call<ResultEntity>

    @GET(Api.FINDBOOLIST)
    fun findBootList(
        @Header("Authorization")token:String,
        @Query("classify") classify:String,
        @Query("pageNum") pageNum:Int,
        @Query("pageSize") pageSize:Int
    ): Call<ResultEntity>
}