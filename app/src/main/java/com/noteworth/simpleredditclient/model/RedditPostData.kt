package com.noteworth.simpleredditclient.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RedditPostData : Serializable {

    var title: String = ""
    var thumbnail: String = ""
    @SerializedName("created_utc")
    var created: Long = 0
    var author: String = ""
    @SerializedName("num_comments")
    var numComments: Long = 0
    var url: String = ""
    var read: Boolean = false
}