package com.mine.common.repository.remote.http.net

import com.mine.common.repository.bean.request.LoginRequest
import com.mine.common.repository.bean.server.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    /**
     * 登录
     */
    @POST("user/login")
    suspend fun login(@Body body: LoginRequest): ApiResponse<LoginResBean?>

    @GET("user/logout/json")
    suspend fun logout(): ApiResponse<Any?>

    /**
     * 文章
     */
    @GET("article/list/{pageNo}/json")
    suspend fun articleList(@Path("pageNo") pageNo: Int): ApiResponse<ArticleBean?>

    /**
     * 轮播图
     */
    @GET("banner/json")
    suspend fun banner(): ApiResponse<List<BannerBean>?>

    /**
     * 常用网站
     */
    @GET("friend/json")
    suspend fun friend():ApiResponse<List<FriendBean>?>
}