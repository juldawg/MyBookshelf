package com.example.mybookshelf.ui.theme.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mybookshelf.ui.theme.NotesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesView(
    viewModel: NotesViewModel = hiltViewModel(),
    bookTitle: String, goBack: () -> Unit,
    initialMode: NotesMode = NotesMode.BROWSING
) {
    val notes by viewModel.getNotes(bookTitle).collectAsState(null)
    if (viewModel.isInitialized) {
        var outputNotes by remember { mutableStateOf(notes ?: "") }
        var notesMode by remember { mutableStateOf(initialMode) }
        Scaffold(
            topBar = {
                TopBar(
                    {
                        when (notesMode) {
                            NotesMode.BROWSING -> goBack()
                            NotesMode.EDITING -> notesMode = NotesMode.BROWSING
                        }
                    },
                    notesMode
                ) { notesMode = NotesMode.EDITING }
            },
            bottomBar = {
                if (notesMode == NotesMode.EDITING) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Button(onClick = {
                            viewModel.update(outputNotes)
                            notesMode = NotesMode.BROWSING
                        }) {
                            Text("Confirm")
                        }
                    }
                }
            }
        ) { padding ->
            Column(modifier = Modifier.padding(vertical = padding.calculateTopPadding())) {
                when (notesMode) {
                    NotesMode.EDITING -> EditableNotes(outputNotes) { outputNotes = it }
                    NotesMode.BROWSING -> Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        text = outputNotes
                    )
                }
            }
        }
    } else {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier
                .size(60.dp)
                .fillMaxSize())
        }
    }
}

enum class NotesMode {
    EDITING, BROWSING
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditableNotes(notes: String, outputNotes: (String) -> Unit) {
    OutlinedTextField(
        value = notes,
        onValueChange = { outputNotes(it) },
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}

@Composable
private fun TopBar(back: () -> Unit, notesMode: NotesMode, didPressEdit: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = back) {
            Icon(Icons.Filled.ArrowBack, "go back")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Your notes", style = MaterialTheme.typography.headlineSmall)
            if (notesMode == NotesMode.BROWSING) IconButton(onClick = didPressEdit) {
                Icon(Icons.Filled.Create, "edit")
            }
        }
    }
}