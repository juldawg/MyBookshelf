package com.example.mybookshelf.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Book
import com.example.domain.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val repository: BookRepository,
) : ViewModel() {
    constructor() : this(BookRepositoryMock())

    var results: List<Book> by mutableStateOf(listOf())
        private set

    var searchCache by mutableStateOf("")
        private set

    fun search(key: String) {
        searchCache = key
        viewModelScope.launch(Dispatchers.IO) {
            results = repository.searchBook(key)
        }
    }
}

class BookRepositoryMock : BookRepository() {
    override fun getBooks(): Flow<List<Book>> {
        TODO("Not yet implemented")
    }

    override fun getBook(title: String): Flow<Book?> {
        TODO("Not yet implemented")
    }

    override fun insertBooks(books: List<Book>) {
        TODO("Not yet implemented")
    }

    override fun updateBook(book: Book) {
        TODO("Not yet implemented")
    }

    override fun searchBook(key: String): List<Book> = listOf(
        Book(
            isbn10 = "1234567890",
            isbn13 = "9781234567890",
            title = "Sample Book",
            authors = listOf("Author 1", "Author 2"),
            publishers = listOf("Publisher 1", "Publisher 2"),
            cover = "https://example.com/cover.jpg",
            numberOfPages = 200,
            rating = 4,
            notes = "This is a sample book for testing purposes."
        ),
        Book(
            isbn10 = "1234567890",
            isbn13 = "9781234567890",
            title = "Sample Book2",
            authors = listOf("Author 1", "Author 2"),
            publishers = listOf("Publisher 1", "Publisher 2"),
            cover = "https://example.com/cover.jpg",
            numberOfPages = 200,
            rating = 4,
            notes = "This is a sample book for testing purposes."
        )
    )
}