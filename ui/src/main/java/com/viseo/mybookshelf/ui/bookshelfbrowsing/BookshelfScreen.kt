package com.viseo.mybookshelf.ui.bookshelfbrowsing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.viseo.mybookshelf.domain.Book
import com.viseo.mybookshelf.ui.booksearch.BookRepositoryMock
import com.viseo.mybookshelf.ui.common.Book
import com.viseo.mybookshelf.ui.common.ProgressIndicator
import com.viseo.mybookshelf.ui.common.theme.MyBookshelfTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfScreen(
    viewModel: BookshelfViewModel = hiltViewModel(),
    onAddBook: () -> Unit,
    onSeeNotes: (String) -> Unit,
    onEditNotes: (String) -> Unit
) {
    val books by viewModel.books.collectAsState(null)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Menu, "Menu")
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "My Bookshelf",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddBook) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) { paddingValues ->
        books?.let {
            BookList(paddingValues, it, viewModel::update, onSeeNotes, onEditNotes)
        } ?: ProgressIndicator()
    }
}

@Composable
private fun BookList(
    it: PaddingValues,
    books: List<Book>,
    didModify: (Book) -> Unit,
    seeNotes: (String) -> Unit,
    editNotes: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(it)) {
        var expandedBookTitle: String? by remember {
            mutableStateOf(null)
        }
        LazyColumn {
            items(books) { book ->
                val isExpanded = expandedBookTitle == book.title
                Book(
                    book,
                    isExpanded,
                    onTap = {
                        expandedBookTitle = if (expandedBookTitle == book.title) null else book.title
                    }
                ) {
                    Rating(onRatingChanged = { didModify(book.copy(rating = it)) }, book.rating ?: 0)
                    AnimatedVisibility(isExpanded) {
                        NotesPreview(book, seeNotes, editNotes)
                    }
                }
            }
        }
    }
}

@Composable
private fun NotesPreview(
    book: Book,
    seeNotes: (String) -> Unit,
    editNotes: (String) -> Unit
) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        book.notes?.let {
            Text("Notes: \n")
            Text(
                it.replace("§", "\n\n"),
                maxLines = 8,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier
                    .clickable { seeNotes(book.title) }
                    .padding(top = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                text = "See all",
                textDecoration = TextDecoration.Underline
            )
        } ?: Button({ editNotes(book.title) }) {
            Text("Add your notes")
        }
    }
}

@Composable
private fun Rating(
    onRatingChanged: (Int) -> Unit,
    ratingValue: Int
) {
    LazyRow {
        items(5) {
            Icon(
                Icons.Rounded.Star,
                contentDescription = "star",
                modifier = Modifier
                    .clickable { onRatingChanged(it + 1) }
                    .padding(vertical = 8.dp),
                tint = if (it < ratingValue) MaterialTheme.colorScheme.primary else
                    MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    MyBookshelfTheme {
        BookshelfScreen(
            viewModel = BookshelfViewModel(BookRepositoryMock()),
            onAddBook = {},
            onSeeNotes = {},
            onEditNotes = {})
    }
}