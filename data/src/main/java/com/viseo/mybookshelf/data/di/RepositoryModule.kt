package com.viseo.mybookshelf.data.di

import com.viseo.mybookshelf.data.database.dao.BookDao
import com.viseo.mybookshelf.data.network.BookSearchAPI
import com.viseo.mybookshelf.data.repository.BookRepositoryImpl
import com.viseo.mybookshelf.domain.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideBookRepository(
        dao: BookDao,
        api: BookSearchAPI
    ): BookRepository =
        BookRepositoryImpl(dao, api)
}