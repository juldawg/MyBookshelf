package com.example.data

import com.example.domain.Book
import com.example.domain.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val bookDao: BookDao) : BookRepository() {
    //override var books: MutableStateFlow<List<Book>> = MutableStateFlow(listOf())

    override fun getBooks(): Flow<List<Book>> = bookDao.getAll().map { it.map(com.example.data.Book::toDomain) }

    override fun insertBooks(vararg books: Book) {
        bookDao.insertAll(*books.map(Book::toRecord).toTypedArray())
        //this.books.update { it + books }
    }
    override fun updateBook(book: Book) {
        bookDao.update(book.toRecord())
        //this.books.value.map { if (it.title == book.title) book else it }
    }
}

fun com.example.data.Book.toDomain(): Book = Book(this.title, this.author, this.rating, this.notes)
fun Book.toRecord(): com.example.data.Book =  com.example.data.Book(this.title, this.author, this.rating, this.notes)