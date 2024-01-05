package com.viseo.mybookshelf.domain

import kotlinx.coroutines.flow.Flow


interface BookRepository {

    val savedBooks: Flow<List<Book>>
    fun getOfTitle(title: String): Flow<Book?>
    fun insert(books: Collection<Book>)
    fun update(book: Book)
    suspend fun findWithIsbn(isbn: String): Collection<Book>
}
