package com.noteworth.simpleredditclient.di

import com.noteworth.simpleredditclient.RedditFeedApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBuilderModule::class,
        ApiModule::class,
        AppModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: RedditFeedApplication): Builder

        fun build(): AppComponent
    }

    fun inject(application: RedditFeedApplication)
}
