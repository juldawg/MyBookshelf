package com.example.data

import com.example.domain.Book
import com.google.gson.annotations.SerializedName

data class BookDto(
    @SerializedName("publishers"      ) val publishers      : ArrayList<Publisher>?,
    @SerializedName("identifiers"     ) val identifiers     : Identifier,
    @SerializedName("title"           ) val title           : String,
    @SerializedName("number_of_pages" ) val numberOfPages   : Int?,
    @SerializedName("cover"           ) val cover           : Cover?,
    @SerializedName("authors"         ) val authors         : ArrayList<Author>?
)

data class Publisher(@SerializedName("name") val name: String)

data class Author(@SerializedName("name") val name: String)

data class Identifier(
    @SerializedName("isbn_10") val isbn10: List<String>?,
    @SerializedName("isbn_13") val isbn13: List<String>?
)

data class Cover(@SerializedName("small") val url: String)

fun BookDto.toBook() = Book(
    identifiers.isbn10?.first(),
    identifiers.isbn13?.first(),
    title,
    authors?.map(Author::name),
    publishers?.map(Publisher::name),
    cover?.url,
    numberOfPages
)
