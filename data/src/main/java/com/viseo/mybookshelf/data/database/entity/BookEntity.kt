package com.viseo.mybookshelf.data.database.entity

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
@Entity(primaryKeys = ["title", "name"])
data class AuthorEntity(
    val title: String,
    val name: String
)
@Entity(primaryKeys = ["title", "name"])
data class PublisherEntity(
    val title: String,
    val name: String
)