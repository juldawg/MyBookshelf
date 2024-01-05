package com.viseo.mybookshelf.ui.booksearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.viseo.mybookshelf.ui.common.Book
import com.viseo.mybookshelf.ui.common.theme.MyBookshelfTheme
import com.viseo.mybookshelf.domain.Book as BookModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSearchScreen(
    goBack: () -> Unit,
    goToDetail: (String) -> Unit,
    viewModel: BookSearchViewModel = hiltViewModel(),
) {
    var searchTerms by rememberSaveable { mutableStateOf("") }
    Scaffold(
        topBar = { TopBar(goBack) },
        content = { padding ->
            Column(Modifier.padding(padding)) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    value = searchTerms,
                    onValueChange = { viewModel.search(it); searchTerms = it },
                    placeholder = { Text("Type in your book id, title, author...") },
                    trailingIcon = { Icon(Icons.Default.Search, "search") },
                    shape = RoundedCornerShape(28.dp)
                )
                LazyColumn {
                    items(viewModel.results) { book ->
                        Book(book = book, onTap = { goToDetail(searchTerms) }) {}
                    }
                }
            }
        }
    )
}

@Composable
private fun TopBar(back: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = back) {
            Icon(Icons.Filled.ArrowBack, "go back")
        }
        Text("Add book", style = MaterialTheme.typography.headlineSmall)
    }
}

@Preview(showBackground = true)
@Composable
fun BookCreationPreview() {
    MyBookshelfTheme {
        BookSearchScreen({}, {}, BookSearchViewModel(
            BookRepositoryMock(), initialResults = listOf(
                BookModel(
                    isbn10 = "1234567890",
                    isbn13 = "9781234567890",
                    title = "Sample Book",
                    authors = listOf("Author 1", "Author 2"),
                    publishers = listOf("Publisher 1", "Publisher 2"),
                    cover = "https://example.com/cover.jpg",
                    numberOfPages = 200,
                    rating = 4,
                    notes = "This is a sample book for testing purposes."
                )
            )
        )
        )
    }
}