package com.example.data

import android.content.Context
import com.example.domain.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideBookRepository(@ApplicationContext context: Context): BookRepository =
        BookRepositoryImpl(AppDatabase.getDatabase(context).bookDao())
}