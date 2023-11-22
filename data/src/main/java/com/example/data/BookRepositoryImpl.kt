package com.example.data

import android.util.Log
import com.example.domain.Book
import com.example.domain.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val bookDao: BookDao) : BookRepository() {

    override fun getBooks(): Flow<List<Book>> =
        bookDao.getAll().map { books -> books.map { it.toDomain() } }

    override fun getBook(id: String): Flow<Book> = bookDao.findById(id).map { it.toDomain() }

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
        val retrofit = Retrofit.Builder()
            .baseUrl("https://openlibrary.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
        val bookSearchApi = retrofit.create(BookSearchAPI::class.java)

        val searchBook = bookSearchApi.searchBook("ISBN:$key")
        Log.d("RetrofitURL", searchBook.request().url().toString())
        val response = searchBook.execute()
        return response.body()?.let { it.values.map(BookDto::toBook) } ?: listOf()
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

fun Book.getDataBook(): com.example.data.Book =
    Book(isbn10, isbn13, title, cover, numberOfPages, rating, notes)

fun Book.getAuthorsEntity(): List<AuthorEntity>? = authors?.map { AuthorEntity(title, it) }

fun Book.getPublishersEntity(): List<PublisherEntity>? =
    publishers?.map { PublisherEntity(title, it) }