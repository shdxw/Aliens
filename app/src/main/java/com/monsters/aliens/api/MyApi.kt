package com.monsters.aliens.api

import com.monsters.aliens.domens.Alien
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyApi {
    @GET("/api/character")
    fun getPosts(): Call<ResponseBody>

    @GET("/api/character")
    fun getExtraPosts(@Query("page") page: Int): Call<ResponseBody>

    @GET("/api/character/{ids}")
    fun getFavorPosts(@Path("ids") ids: String): Call<ResponseBody>
}