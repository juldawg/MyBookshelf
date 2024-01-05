package com.viseo.mybookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viseo.mybookshelf.ui.booksearch.BookSearchScreen
import com.viseo.mybookshelf.ui.bookshelfbrowsing.BookshelfScreen
import com.viseo.mybookshelf.ui.common.BookDetails
import com.viseo.mybookshelf.ui.common.theme.MyBookshelfTheme
import com.viseo.mybookshelf.ui.notesmanagement.NotesScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBookshelfTheme {
                MainView()
            }
        }
    }
}

@Composable
fun MainView() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            BookshelfScreen(
                onAddBook = { navController.navigate("addBook") },
                onSeeNotes = { book ->
                    navController.navigate(
                        "seeNotes/$book"
                    )
                },
                onEditNotes = { book ->
                    navController.navigate(
                        "editNotes/$book"
                    )
                })
        }
        composable("addBook") {
            BookSearchScreen(
                goBack = { navController.popBackStack() },
                goToDetail = { book -> navController.navigate("bookDetails/$book") })
        }
        composable("seeNotes/{book}") { backStackEntry ->
            backStackEntry.arguments?.getString("book")?.let {
                NotesScreen(
                    bookTitle = it,
                    goBack = { navController.popBackStack() },
                    initialMode = com.viseo.mybookshelf.ui.notesmanagement.NotesMode.BROWSING
                )
            }
        }
        composable("editNotes/{book}") { backStackEntry ->
            backStackEntry.arguments?.getString("book")?.let {
                NotesScreen(
                    bookTitle = it,
                    goBack = { navController.popBackStack() },
                    initialMode = com.viseo.mybookshelf.ui.notesmanagement.NotesMode.EDITING
                )
            }
        }
        composable("bookDetails/{book}") { backStackEntry ->
            backStackEntry.arguments?.getString("book")?.let {
                BookDetails(
                    goBack = { navController.popBackStack() },
                    it
                )
            }
        }
    }

}
