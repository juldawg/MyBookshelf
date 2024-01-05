package com.viseo.mybookshelf.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.viseo.mybookshelf.data.database.dao.BookDao
import com.viseo.mybookshelf.data.database.entity.AuthorEntity
import com.viseo.mybookshelf.data.database.entity.Book
import com.viseo.mybookshelf.data.database.entity.PublisherEntity

@Database(
    entities = [Book::class, AuthorEntity::class, PublisherEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                    .also { INSTANCE = it }
            }
    }
}