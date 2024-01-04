package com.viseo.mybookshelf.ui.booksearch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viseo.mybookshelf.domain.Book
import com.viseo.mybookshelf.domain.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val repository: BookRepository,
) : ViewModel() {

    var results: List<Book> by mutableStateOf(listOf())
        private set

    fun search(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            results = repository.searchBook(key)
        }
    }
}