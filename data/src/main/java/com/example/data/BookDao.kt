package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): Flow<List<Book>>

    @Query("SELECT * FROM book WHERE title LIKE :title LIMIT 1")
    fun findByName(title: String): Book

    @Insert
    fun insertAll(vararg books: Book)

    @Update
    fun update(book: Book)

    @Delete
    fun delete(book: Book)
}