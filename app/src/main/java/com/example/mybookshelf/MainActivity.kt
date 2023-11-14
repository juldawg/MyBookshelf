package com.example.mybookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.domain.Book
import com.example.mybookshelf.ui.theme.BookshelfUiState
import com.example.mybookshelf.ui.theme.BookshelfViewModel
import com.example.mybookshelf.ui.theme.MyBookshelfTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private val viewModel: BookshelfViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchBooks()
            }
        }
        setContent {
            MyBookshelfTheme {
                MainView(viewModel)
            }
        }
    }
}
@Composable
fun Book(book: Book, modify: (Book) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.clickable { isExpanded = !isExpanded }.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(book.title, style = MaterialTheme.typography.labelLarge)
                Text(
                    book.author,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
                AnimatedVisibility (isExpanded) {
                    Text(book.notes.let { it.ifEmpty { "Add your notes" } }, maxLines = 5)

                }
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
    }
}

@Composable
fun MainView(viewModel: BookshelfViewModel) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { Home(viewModel.uiState, { navController.navigate("addBook") }, { viewModel.update(it) })  }
        composable("addBook") { BookCreationView({ book -> viewModel.add(book) }, { navController.popBackStack() })
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(uiState: BookshelfUiState, didPressAddButton: () -> Unit, didModify: (Book) -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { Column(modifier = Modifier.padding(16.dp)) { Text("My Bookshelf", style = MaterialTheme.typography.headlineSmall) } },
        floatingActionButton = {
            FloatingActionButton(onClick = didPressAddButton) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(it)) {
            LazyColumn {
                items(uiState.books) { book ->
                    Book(book, didModify)
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
        Home(uiState = BookshelfUiState(books = previewBooks), {}) {}
    }
}
