package com.example.mybookshelf.ui.theme

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Book
import com.example.domain.BookRepository
import dagger.Provides
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookshelfViewModel @Inject constructor(
    private val repository: BookRepository,
) : ViewModel() {
    var uiState by mutableStateOf(BookshelfUiState(listOf()))
        private set

    private var fetchJob: Job? = null

    fun fetchBooks() {
        fetchJob?.cancel()
        fetchJob = CoroutineScope(Dispatchers.IO).launch {
            repository.getBooks().onEach { uiState = uiState.copy(books = it) }.collect()
        }
    }

    fun update(book: Book) {
        repository.updateBook(book)
    }

    fun add(book: Book) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertBooks(book)
        }
    }

}