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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchDetailsViewModel @Inject constructor(
    private val repository: BookRepository,
) : ViewModel() {
    constructor() : this(BookRepositoryMock())

    private var book: Book? by mutableStateOf(null)

    val isAddButtonEnabled: Flow<Boolean> = flow {
        while(true) {
            book?.let { book ->
                repository.getOfTitle(book.title).collect {
                    emit(it == null)
                }

            } ?: emit(false)
            delay(100)
        }
    }

    fun search(key: String): Flow<Book?> = flow {
        book = repository.findWithIsbn(key).firstOrNull()
        emit(book)
    }

    fun addToBookshelf() {
        viewModelScope.launch(Dispatchers.IO) {
            book?.let { repository.insert(listOf(it)) }
        }
    }
}