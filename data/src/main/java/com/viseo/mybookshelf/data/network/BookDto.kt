package com.viseo.mybookshelf.data.network

import com.google.gson.annotations.SerializedName
import com.viseo.mybookshelf.domain.Book

data class BookDto(
    @SerializedName("publishers"      ) val publisherDtos      : ArrayList<PublisherDto>?,
    @SerializedName("identifiers"     ) val identifiers     : IdentifierDto,
    @SerializedName("title"           ) val title           : String,
    @SerializedName("number_of_pages" ) val numberOfPages   : Int?,
    @SerializedName("cover"           ) val coverDto           : CoverDto?,
    @SerializedName("authors"         ) val authorDtos         : ArrayList<AuthorDto>?
)

data class PublisherDto(@SerializedName("name") val name: String)

data class AuthorDto(@SerializedName("name") val name: String)

data class IdentifierDto(
    @SerializedName("isbn_10") val isbn10: List<String>?,
    @SerializedName("isbn_13") val isbn13: List<String>?
)

data class CoverDto(@SerializedName("small") val url: String)

fun BookDto.toBook() = Book(
    identifiers.isbn10?.first(),
    identifiers.isbn13?.first(),
    title,
    authorDtos?.map(AuthorDto::name) ?: listOf(),
    publisherDtos?.map(PublisherDto::name) ?: listOf(),
    coverDto?.url,
    numberOfPages
)
