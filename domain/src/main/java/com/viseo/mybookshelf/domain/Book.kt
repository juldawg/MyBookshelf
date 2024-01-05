package com.viseo.mybookshelf.domain

data class Book(
    val isbn10: String?,
    val isbn13: String?,
    val title: String,
    val authors: List<String>,
    val publishers: List<String>,
    val cover: String?,
    val numberOfPages: Int?,
    val rating: Int? = null,
    val notes: String? = null
)
