package com.example.mybookshelf

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybookshelf.ui.theme.BookshelfViewModel
import com.example.mybookshelf.ui.theme.MyBookshelfTheme
import com.example.mybookshelf.ui.theme.composables.BookCreationView
import com.example.mybookshelf.ui.theme.composables.BookDetails
import com.example.mybookshelf.ui.theme.composables.Home
import com.example.mybookshelf.ui.theme.composables.NotesMode
import com.example.mybookshelf.ui.theme.composables.NotesView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: BookshelfViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBookshelfTheme {
                MainView(viewModel)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainView(viewModel: BookshelfViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val books by viewModel.books.collectAsState(null)
            books?.let {
                Home(
                    it,
                    { navController.navigate("addBook") },
                    viewModel::update,
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
        }
        composable("addBook") {
            BookCreationView(
                { navController.popBackStack() },
                { book -> navController.navigate("bookDetails/$book") })
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
        composable("bookDetails/{book}") {backStackEntry ->
            backStackEntry.arguments?.getString("book")?.let {
                BookDetails(goBack = { navController.popBackStack() }, it)
            }
        }
    }

}
