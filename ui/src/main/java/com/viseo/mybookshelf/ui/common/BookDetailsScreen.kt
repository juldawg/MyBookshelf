package com.viseo.mybookshelf.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.viseo.mybookshelf.domain.Book
import com.viseo.mybookshelf.ui.R
import com.viseo.mybookshelf.ui.booksearch.BookSearchDetailsViewModel
import com.viseo.mybookshelf.ui.common.extensions.getAuthors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetails(
    goBack: () -> Unit,
    bookId: String,
    viewModel: BookSearchDetailsViewModel = hiltViewModel()
) {
    val book by viewModel.search(bookId).collectAsState(initial = null)
    Scaffold(
        topBar = { TopBar(goBack) },
        content = { padding ->
            book?.let {
                Details(padding, it)
            } ?: ProgressIndicator()
        },
        bottomBar = {
            val isEnabled by viewModel.isAddButtonEnabled.collectAsState(initial = false)
            // Add to Bookshelf Button
            Button(
                onClick = {
                    viewModel.addToBookshelf()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = isEnabled
            ) {
                Text(text = "Add to your bookshelf")
            }
        }
    )
}

@Composable
private fun Details(
    padding: PaddingValues,
    book: Book
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
    ) {
        // Book Cover
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
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(200.dp)
        )

        // Book Title
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Book Authors/Publishers
        Text(
            text = book.getAuthors(),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Other Book Information
        book.isbn10?.let {
            Text(text = "ISBN-10: $it", modifier = Modifier.padding(bottom = 4.dp))
        }

        book.isbn13?.let {
            Text(text = "ISBN-13: $it", modifier = Modifier.padding(bottom = 4.dp))
        }

        book.numberOfPages?.let {
            Text(
                text = "Number of Pages: $it",
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
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
        Text("Details", style = MaterialTheme.typography.headlineSmall)
    }
}

@Preview(showBackground = true)
@Composable
fun BookDetailsPreview() {
    BookDetails(
        goBack = {},
        bookId = "dumbId",
        viewModel = BookSearchDetailsViewModel()
    )
}