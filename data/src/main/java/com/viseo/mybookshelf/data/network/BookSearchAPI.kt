package com.viseo.mybookshelf.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface BookSearchAPI {
    @GET("api/books?jscmd=data&format=json")
    suspend fun searchBook(@Query("bibkeys") isbn: String): Map<String, BookDto>
}