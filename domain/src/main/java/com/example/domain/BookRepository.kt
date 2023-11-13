package com.example.domain

import kotlinx.coroutines.flow.Flow


abstract class BookRepository {
    //abstract val books: Flow<List<Book>>
    abstract fun getBooks(): Flow<List<Book>>
    abstract fun insertBooks(vararg books: Book)
    abstract fun updateBook(book: Book)
}
