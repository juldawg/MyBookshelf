package com.viseo.mybookshelf.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.viseo.mybookshelf.domain.Book
import com.viseo.mybookshelf.ui.R
import com.viseo.mybookshelf.ui.common.extensions.getAuthors

@Composable
fun Book(
    book: Book,
    isFocused: Boolean = false,
    onTap: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(
            if (isFocused) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .clickable { onTap() }
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
                        book.getAuthors(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            content()
        }
    }
}