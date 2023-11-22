package com.example.mybookshelf.ui.theme.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.Book
import com.example.mybookshelf.ui.theme.BookSearchViewModel
import com.example.mybookshelf.ui.theme.MyBookshelfTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCreationView(
    add: (Book) -> Unit,
    goBack: () -> Unit,
    viewModel: BookSearchViewModel = hiltViewModel()
) {
    var search by remember { mutableStateOf("") }
    Scaffold(
        topBar = { TopBar(goBack) },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Column(Modifier.padding(8.dp)) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        value = search,
                        onValueChange = { search = it; viewModel.search(it) },
                        placeholder = { Text("Type in your book id, title, author...") },
                        trailingIcon = { Icon(Icons.Default.Search, "search") },
                        shape = RoundedCornerShape(28.dp)
                    )
                    LazyColumn {
                        items(viewModel.results) { book ->
                            Book(book)
                        }
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

@Composable
private fun Book(
    book: Book
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(book.title, style = MaterialTheme.typography.titleLarge)
                    Text(
                        book.authors?.joinToString() ?: book.publishers?.joinToString()
                        ?: "unknown",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookCreationPreview() {
    MyBookshelfTheme {
        BookCreationView({}, {})
    }
}