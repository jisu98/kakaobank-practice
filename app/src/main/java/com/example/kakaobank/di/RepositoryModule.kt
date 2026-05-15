package com.example.kakaobank.di

import android.content.Context
import android.content.SharedPreferences
import com.example.kakaobank.data.repository.BookmarkRepositoryImpl
import com.example.kakaobank.data.repository.SearchRepositoryImpl
import com.example.kakaobank.domain.repository.IBookmarkRepository
import com.example.kakaobank.domain.repository.ISearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): ISearchRepository

    @Binds
    @Singleton
    abstract fun bindBookmarkRepository(impl: BookmarkRepositoryImpl): IBookmarkRepository

    companion object {
        @Provides
        @Singleton
        fun provideSharedPreferences(
            @ApplicationContext context: Context,
        ): SharedPreferences = context.getSharedPreferences("kakaobank_prefs", Context.MODE_PRIVATE)
    }
}
