package com.noteworth.simpleredditclient.api;

import com.noteworth.simpleredditclient.model.RedditTop;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RedditService {

    @GET("/top.json")
    Call<RedditTop> getTop();
}
