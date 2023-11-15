package com.example.mybookshelf.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Book
import com.example.domain.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: BookRepository,
) : ViewModel() {
    private val book: MutableStateFlow<Book?> = MutableStateFlow(null)
    val notes: MutableStateFlow<String> = MutableStateFlow("")
    //private var fetchJob: Job? = null
    fun fetchBook(title: String) {
        viewModelScope.launch {
            repository.getBook(title).onEach {
                book.value = it
                notes.value = it.notes
            }.collect()
        }
    }

    fun update(notes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            book.value?.let { repository.updateBook(it.copy(notes = notes)) }
        }
    }
}