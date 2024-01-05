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
    @ColumnInfo(name = "isbn10") val isbn10: String?,
    @ColumnInfo(name = "isbn13") val isbn13: String?,
    @PrimaryKey @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "cover") val cover: String?,
    @ColumnInfo(name = "number_of_pages") val numberOfPages: Int?,
    @ColumnInfo(name = "rating") val rating: Int? = null,
    @ColumnInfo(name = "notes") val notes: String? = null
)

@Entity(primaryKeys = ["title", "name"])
data class AuthorEntity(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "name") val name: String
)

@Entity(primaryKeys = ["title", "name"])
data class PublisherEntity(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "name") val name: String
)
