package com.noteworth.simpleredditclient.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noteworth.simpleredditclient.model.RedditTop
import com.noteworth.simpleredditclient.repository.RedditRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditViewModel @Inject constructor(private val redditRepository: RedditRepository) :
    ViewModel(),
    RefreshableViewModel {

    private val redditTopLiveData: MutableLiveData<RedditTop> = MutableLiveData()

    fun getData() = redditRepository.getRedditTop(redditTopLiveData)

    fun getRedditTopLiveData(): LiveData<RedditTop> = redditTopLiveData

    override fun refresh() {
        getData()
    }
}
