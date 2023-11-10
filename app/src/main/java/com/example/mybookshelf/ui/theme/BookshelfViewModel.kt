package com.example.mybookshelf.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.BookRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookshelfViewModel @Inject constructor(
    private val repository: BookRepository,
) : ViewModel() {
    var uiState by mutableStateOf(BookshelfUiState(listOf()))
        private set

    private var fetchJob: Job? = null

    fun fetchBooks() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val books = repository.books
            uiState = uiState.copy(books = books)
        }
    }
}