package com.viseo.mybookshelf.data.repository

import com.viseo.mybookshelf.data.database.entity.AuthorEntity
import com.viseo.mybookshelf.data.database.dao.BookDao
import com.viseo.mybookshelf.data.network.BookDto
import com.viseo.mybookshelf.data.database.entity.BookEntity
import com.viseo.mybookshelf.data.network.BookSearchAPI
import com.viseo.mybookshelf.data.database.entity.PublisherEntity
import com.viseo.mybookshelf.data.network.toBook
import com.viseo.mybookshelf.domain.Book
import com.viseo.mybookshelf.domain.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookDao: BookDao,
    private val bookSearchAPI: BookSearchAPI
) : BookRepository() {

    override fun getBooks(): Flow<List<Book>> =
        bookDao.getAll().map { books -> books.map { it.toDomain() } }

    override fun getBook(title: String): Flow<Book?> =
        bookDao.findByTitle(title).map { it?.toDomain() }

    override fun insertBooks(books: List<Book>) {
        bookDao.insertBooks(books.map(Book::getDataBook))
        books.map(Book::getAuthorsEntity)
            .forEach { it?.let { authors -> bookDao.insertAuthors(authors) } }
        books.map(Book::getPublishersEntity)
            .forEach { it?.let { publishers -> bookDao.insertPublishers(publishers) } }
    }

    override fun updateBook(book: Book) {
        bookDao.update(book.getDataBook())
    }

    override fun searchBook(key: String): List<Book> {
        val searchBook = bookSearchAPI.searchBook("ISBN:$key")
        val response = searchBook.execute()
        return response.body()?.values?.map(BookDto::toBook) ?: listOf()
    }
}

fun BookEntity.toDomain(): Book =
    Book(
        book.isbn10,
        book.isbn13,
        book.title,
        authors,
        publishers,
        book.cover,
        book.numberOfPages,
        book.rating,
        book.notes
    )

fun Book.getDataBook(): com.viseo.mybookshelf.data.database.entity.Book =
    com.viseo.mybookshelf.data.database.entity.Book(
        isbn10,
        isbn13,
        title,
        cover,
        numberOfPages,
        rating,
        notes
    )

fun Book.getAuthorsEntity(): List<AuthorEntity>? = authors?.map { AuthorEntity(title, it) }

fun Book.getPublishersEntity(): List<PublisherEntity>? =
    publishers?.map { PublisherEntity(title, it) }