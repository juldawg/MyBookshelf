package com.example.mybookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybookshelf.ui.theme.BookshelfViewModel
import com.example.mybookshelf.ui.theme.MyBookshelfTheme
import com.example.mybookshelf.ui.theme.composables.BookCreationView
import com.example.mybookshelf.ui.theme.composables.Home
import com.example.mybookshelf.ui.theme.composables.NotesMode
import com.example.mybookshelf.ui.theme.composables.NotesView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
fun MainView(viewModel: BookshelfViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            Home(
                viewModel.uiState,
                { navController.navigate("addBook") },
                { viewModel.update(it) },
                { book ->
                    navController.navigate(
                        "seeNotes/$book"
                    )
                },
                { book ->
                    navController.navigate(
                        "editNotes/$book"
                    )
                })
        }
        composable("addBook") {
            BookCreationView(
                { book -> viewModel.add(book) },
                { navController.popBackStack() })
        }
        composable("seeNotes/{book}") { backStackEntry ->
            backStackEntry.arguments?.getString("book")?.let {
                NotesView(
                    bookTitle = it,
                    goBack = { navController.popBackStack() },
                    initialMode = NotesMode.BROWSING
                )
            }
        }
        composable("editNotes/{book}") { backStackEntry ->
            backStackEntry.arguments?.getString("book")?.let {
                NotesView(
                    bookTitle = it,
                    goBack = { navController.popBackStack() },
                    initialMode = NotesMode.EDITING
                )
            }
        }
    }

}
