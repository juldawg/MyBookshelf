package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Transaction
    @Query("SELECT * FROM book")
    fun getAll(): Flow<List<BookEntity>>

    @Transaction
    @Query("SELECT * FROM book WHERE isbn10 LIKE :id OR isbn13 LIKE :id LIMIT 1")
    fun findById(id: String): Flow<BookEntity?>

    @Transaction
    @Query("SELECT * FROM book WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): Flow<BookEntity?>

    @Insert
    fun insertBooks(books: List<Book>)

    @Insert
    fun insertAuthors(authors: List<AuthorEntity>)

    @Insert
    fun insertPublishers(publishers: List<PublisherEntity>)

    @Update
    fun update(book: Book)

    @Delete
    fun delete(book: Book)
}