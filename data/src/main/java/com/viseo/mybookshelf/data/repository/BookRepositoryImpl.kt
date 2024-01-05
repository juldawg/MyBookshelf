package com.viseo.mybookshelf.data.repository

import com.viseo.mybookshelf.data.database.dao.BookDao
import com.viseo.mybookshelf.data.database.entity.AuthorEntity
import com.viseo.mybookshelf.data.database.entity.BookEntity
import com.viseo.mybookshelf.data.database.entity.PublisherEntity
import com.viseo.mybookshelf.data.network.BookDto
import com.viseo.mybookshelf.data.network.BookSearchAPI
import com.viseo.mybookshelf.data.network.toBook
import com.viseo.mybookshelf.domain.Book
import com.viseo.mybookshelf.domain.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.viseo.mybookshelf.data.database.entity.Book as DataBook

class BookRepositoryImpl @Inject constructor(
    private val bookDao: BookDao,
    private val bookSearchAPI: BookSearchAPI
) : BookRepository {
    override val savedBooks: Flow<List<Book>>
        get() = bookDao.getAll().map { it.map(BookEntity::toDomainEntity) }

    override fun getOfTitle(title: String): Flow<Book?> =
        bookDao.findByTitle(title).map { it?.toDomainEntity() }

    override fun insert(books: Collection<Book>) {
        bookDao.insertBooks(books.map(Book::toDataEntity))
        books.map(Book::getAuthorsEntity)
            .forEach { bookDao.insertAuthors(it) }
        books.map(Book::getPublishersEntity)
            .forEach { bookDao.insertPublishers(it) }
    }

    override fun update(book: Book) = bookDao.update(book.toDataEntity())

    override suspend fun findWithIsbn(isbn: String): Collection<Book> =
        bookSearchAPI.searchBook("ISBN:$isbn")
            .values
            .map(BookDto::toBook)
}

fun BookEntity.toDomainEntity(): Book =
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

fun Book.toDataEntity(): DataBook =
    DataBook(
        isbn10,
        isbn13,
        title,
        cover,
        numberOfPages,
        rating,
        notes
    )

fun Book.getAuthorsEntity(): List<AuthorEntity> = authors.map { AuthorEntity(title, it) }

fun Book.getPublishersEntity(): List<PublisherEntity> =
    publishers.map { PublisherEntity(title, it) }