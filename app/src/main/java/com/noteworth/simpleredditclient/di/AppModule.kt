package com.noteworth.simpleredditclient.di

import android.content.Context
import com.noteworth.simpleredditclient.RedditFeedApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: RedditFeedApplication): Context = application
}
