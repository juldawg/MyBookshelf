package com.example.mybookshelf.ui.theme.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.Book
import com.example.mybookshelf.ui.theme.MyBookshelfTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    books: List<Book>,
    didPressAddButton: () -> Unit,
    didModify: (Book) -> Unit,
    seeNotes: (String) -> Unit,
    editNotes: (String) -> Unit
) {
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
            FloatingActionButton(onClick = didPressAddButton) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) {
        BookList(it, books, didModify, seeNotes, editNotes)
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
        var expanded: String? by remember {
            mutableStateOf(null)
        }
        LazyColumn {
            items(books) { book ->
                Book(
                    book,
                    didModify,
                    seeNotes,
                    editNotes,
                    expanded == book.title,
                    toggleIsExpanded = { expanded = if (expanded == book.title) null else book.title }
                )
            }
        }
    }
}

@Composable
private fun Book(
    book: Book,
    modify: (Book) -> Unit,
    seeNotes: (String) -> Unit,
    editNotes: (String) -> Unit,
    isExpanded: Boolean,
    toggleIsExpanded: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(
            if (isExpanded) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .clickable { toggleIsExpanded() }
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(book.title, style = MaterialTheme.typography.titleLarge)
                    Text(
                        book.author,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                LazyRow {
                    items(5) {
                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = "star",
                            modifier = Modifier.clickable { modify(book.copy(rating = it + 1)) },
                            tint = if (it < book.rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
            AnimatedVisibility(isExpanded) {
                Column(modifier = Modifier.padding(top = 24.dp)) {
                    if (book.notes.isNotEmpty()) {
                        Text(book.notes, maxLines = 5)
                        Text(
                            modifier = Modifier
                                .clickable { seeNotes(book.title) }
                                .padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            text = "See all",
                            textDecoration = TextDecoration.Underline
                        )
                    } else {
                        Button({ editNotes(book.title) }) {
                            Text("Add your notes")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val previewBooks = listOf(
        Book("Capital et Idéologie", "Thomas Piketty", 3, "Plutôt cool"),
        Book("Le Pouvoir Rhétorique", "Clément Viktorovich", 2, "Un peu naze"),
        Book("Bureaucratie", "David Graeber", 5, "Un truc de ouf !"),
        Book("Bullshit Job", "David Graeber", 5, "Super super cooool")
    ).sortedBy(Book::title)
    MyBookshelfTheme {
        Home(previewBooks, {}, {}, {}) {}
    }
}