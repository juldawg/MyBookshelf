package com.example.domain

import kotlinx.coroutines.flow.Flow


abstract class BookRepository {
    //abstract val books: Flow<List<Book>>
    abstract fun getBooks(): Flow<List<Book>>
    abstract fun getBook(title: String): Flow<Book?>
    abstract fun insertBooks(books: List<Book>)
    abstract fun updateBook(book: Book)

    abstract fun searchBook(key: String): List<Book>
}
