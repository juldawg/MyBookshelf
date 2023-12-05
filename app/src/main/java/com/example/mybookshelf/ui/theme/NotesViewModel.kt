package com.example.mybookshelf.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Book
import com.example.domain.BookRepository
import com.example.mybookshelf.ui.theme.uistate.NotesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: BookRepository,
) : ViewModel() {

    private var book: Book? by mutableStateOf(null)

    fun getNotes(bookTitle: String): Flow<NotesUiState> {
        val bookFlow = repository.getBook(bookTitle)
        viewModelScope.launch {
            bookFlow.collect {
                book = it
            }
        }
        return bookFlow.map { NotesUiState(it?.notes) }
    }


    fun update(notes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            book?.let { repository.updateBook(it.copy(notes = notes)) }
        }
    }
}