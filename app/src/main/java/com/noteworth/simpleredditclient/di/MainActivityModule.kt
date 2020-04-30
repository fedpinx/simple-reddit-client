package com.noteworth.simpleredditclient.di

import com.noteworth.simpleredditclient.ui.fragment.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun provideMainFragment(): MainFragment
}