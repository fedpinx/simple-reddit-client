package com.noteworth.simpleredditclient.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.noteworth.simpleredditclient.R
import com.noteworth.simpleredditclient.api.RedditService
import com.noteworth.simpleredditclient.model.RedditTop
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RedditRepository @Inject
constructor(private val context: Context, private val redditService: RedditService) {

    fun getRedditTop(topMutableLiveData: MutableLiveData<RedditTop>) {
        redditService.top.enqueue(object : Callback<RedditTop> {
            override fun onResponse(call: Call<RedditTop>, response: Response<RedditTop>) {
                if (response.isSuccessful) {
                    topMutableLiveData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<RedditTop>, t: Throwable) {
                Toast.makeText(
                    context, context.getString(R.string.network_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
