package com.example.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class BookEntity(
    @Embedded val book: Book,
    @Relation(
        parentColumn = "title",
        entityColumn = "title",
        entity = AuthorEntity::class,
        projection = ["name"]
    )
    val authors: List<String>,
    @Relation(
        parentColumn = "title",
        entityColumn = "title",
        entity = PublisherEntity::class,
        projection = ["name"]
    )
    val publishers: List<String>
)
@Entity
data class Book(
    val isbn10: String?,
    val isbn13: String?,
    @PrimaryKey val title: String,
    val cover: String?,
    @ColumnInfo(name = "number_of_pages") val numberOfPages: Int?,
    val rating: Int? = null,
    val notes: String? = null
)
@Entity
data class AuthorEntity(
    val title: String,
    @PrimaryKey val name: String
)
@Entity
data class PublisherEntity(
    val title: String,
    @PrimaryKey val name: String
)