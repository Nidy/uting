package com.uting.api

import com.uting.ui.home.entity.ChapterInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
interface ApiService {

    @FormUrlEncoded
    @POST("/getChapter")
    fun getChapters(
        @Field("pageNum") pageNum: Int,
        @Field("pageSize") pageSize: Int
    ): Call<BaseResponse<List<ChapterInfo>>>
}