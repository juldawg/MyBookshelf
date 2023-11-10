package com.example.mybookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.Book
import com.example.mybookshelf.ui.theme.BookshelfUiState
import com.example.mybookshelf.ui.theme.BookshelfViewModel
import com.example.mybookshelf.ui.theme.MyBookshelfTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: BookshelfViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBookshelfTheme {
                MainView(viewModel.uiState)
            }
        }
    }
}
@Composable
fun Book(book: Book) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        var rating by remember { mutableStateOf(book.rating) }
        Row(
            modifier = Modifier.clickable { isExpanded = !isExpanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(all = 8.dp)) {
                Text(book.title, style = MaterialTheme.typography.labelLarge)
                Text(
                    book.author,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
                AnimatedVisibility (isExpanded) {
                    Text(book.notes)
                }
            }
            LazyRow {
                items(5) {
                    Icon(
                        Icons.Rounded.Star,
                        contentDescription = "star",
                        modifier = Modifier.clickable { rating = it + 1 },
                        tint = if (it < rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                }
            }
        }
    }
}

@Composable
fun MainView(uiState: BookshelfUiState) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn {
            items(uiState.books) { book ->
                Book(book)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val previewBooks = listOf(
        Book("Capital et Idéologie", "Thomas Piketty", 3, "Plutôt cool"),
        Book("Le Pouvoir Rhétorique", "Clément Viktorovich", 2, "Un peu naze"),
        Book("Bureaucratie", "David Graeber", 5, "Un truc de ouf !"),
        Book("Bullshit Job", "David Graeber", 5, "Super super cooool")
    ).sortedBy(Book::title)
    MyBookshelfTheme {
        MainView(BookshelfUiState(books = previewBooks))
    }
}