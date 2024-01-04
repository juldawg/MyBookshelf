package com.viseo.mybookshelf.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookSearchAPI {
    @GET("api/books?jscmd=data&format=json")
    fun searchBook(@Query("bibkeys") isbn: String): Call<Map<String, BookDto>>
}