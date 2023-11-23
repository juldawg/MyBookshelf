package com.example.data

import android.content.Context
import com.example.domain.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideBookRepository(
        @ApplicationContext context: Context,
        api: BookSearchAPI
    ): BookRepository =
        BookRepositoryImpl(AppDatabase.getDatabase(context).bookDao(), api)
}