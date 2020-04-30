package com.noteworth.simpleredditclient.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.noteworth.simpleredditclient.viewmodel.RedditViewModel
import com.noteworth.simpleredditclient.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RedditViewModel::class)
    abstract fun bindRedditViewModel(redditViewModel: RedditViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
