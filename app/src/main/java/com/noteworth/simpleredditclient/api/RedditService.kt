package com.noteworth.simpleredditclient.api

import com.noteworth.simpleredditclient.model.RedditTop
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditService {

    @GET("/r/all/top/.json?t=all")
    fun getTop(
        @Query("limit") limit: Int = 10,
        @Query("after") afterKey: String? = null,
        @Query("before") beforeKey: String? = null
    ): Call<RedditTop>
}
