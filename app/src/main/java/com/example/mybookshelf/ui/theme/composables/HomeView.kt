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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.domain.Book
import com.example.mybookshelf.R
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
                    toggleIsExpanded = {
                        expanded = if (expanded == book.title) null else book.title
                    }
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(
                            book.cover
                                ?: "https://static.vecteezy.com/system/resources/previews/024/043/963/original/book-icon-clipart-transparent-background-free-png.png"
                        )
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.baseline_menu_book_24),
                    contentDescription = "book cover",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(60.dp)
                )
                Column {
                    Text(book.title, style = MaterialTheme.typography.titleLarge)
                    Text(
                        book.authors?.joinToString() ?: book.publishers?.joinToString()
                        ?: "unknown",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    LazyRow {
                        items(5) {
                            Icon(
                                Icons.Rounded.Star,
                                contentDescription = "star",
                                modifier = Modifier
                                    .clickable { modify(book.copy(rating = it + 1)) }
                                    .padding(vertical = 8.dp),
                                tint = if (it < (book.rating
                                        ?: 0)
                                ) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(isExpanded) {
                Column(modifier = Modifier.padding(top = 24.dp)) {
                    book.notes?.let {
                        Text(it, maxLines = 5, overflow = TextOverflow.Ellipsis)
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val previewBooks = listOf(
        Book(
            isbn10 = "0123456789",
            isbn13 = "9780123456789",
            title = "The Catcher in the Rye",
            authors = listOf("J.D. Salinger"),
            publishers = listOf("Little, Brown and Company"),
            cover = "https://example.com/catcher-in-the-rye.jpg",
            numberOfPages = 224,
            rating = 4,
            notes = "Classic coming-of-age novel."
        ),
        Book(
            isbn10 = "9876543210",
            isbn13 = "9789876543210",
            title = "To Kill a Mockingbird",
            authors = listOf("Harper Lee"),
            publishers = listOf("J.B. Lippincott & Co."),
            cover = "https://example.com/to-kill-a-mockingbird.jpg",
            numberOfPages = 281,
            rating = 5,
            notes = "Powerful exploration of racial injustice."
        ),
        Book(
            isbn10 = "0123456781",
            isbn13 = "9780123456781",
            title = "1984",
            authors = listOf("George Orwell"),
            publishers = listOf("Secker & Warburg"),
            cover = "https://example.com/1984.jpg",
            numberOfPages = 328,
            rating = 5,
            notes = "Dystopian masterpiece."
        ),
        Book(
            isbn10 = "9876543211",
            isbn13 = "9789876543211",
            title = "The Great Gatsby",
            authors = listOf("F. Scott Fitzgerald"),
            publishers = listOf("Charles Scribner's Sons"),
            cover = "https://example.com/the-great-gatsby.jpg",
            numberOfPages = 180,
            rating = 4,
            notes = "Classic American novel."
        ),
        Book(
            isbn10 = "0123456782",
            isbn13 = "9780123456782",
            title = "Pride and Prejudice",
            authors = listOf("Jane Austen"),
            publishers = listOf("T. Egerton, Whitehall"),
            cover = "https://example.com/pride-and-prejudice.jpg",
            numberOfPages = 279,
            rating = 5,
            notes = "Romantic novel of manners."
        ),
        Book(
            isbn10 = "9876543212",
            isbn13 = "9789876543212",
            title = "One Hundred Years of Solitude",
            authors = listOf("Gabriel García Márquez"),
            publishers = listOf("Harper & Row"),
            cover = "https://example.com/one-hundred-years-of-solitude.jpg",
            numberOfPages = 417,
            rating = 5,
            notes = "Magical realism at its finest."
        )
    ).sortedBy(Book::title)
    MyBookshelfTheme {
        Home(previewBooks, {}, {}, {}) {}
    }
}