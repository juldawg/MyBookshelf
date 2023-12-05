package com.example.mybookshelf.ui.theme.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mybookshelf.R
import com.example.mybookshelf.ui.theme.BookRepositoryMock
import com.example.mybookshelf.ui.theme.NotesViewModel
import com.example.mybookshelf.ui.theme.animations.pageAnimation
import com.example.mybookshelf.ui.theme.composables.common.ProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NotesView(
    viewModel: NotesViewModel = hiltViewModel(),
    bookTitle: String,
    goBack: () -> Unit,
    initialMode: NotesMode = NotesMode.BROWSING
) {
    val uiState by viewModel.getNotes(bookTitle).collectAsState(null)
    if (uiState != null) {
        Notes(uiState!!.notes, initialMode, goBack, viewModel)
    } else {
        ProgressIndicator()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun Notes(
    notes: String?, initialMode: NotesMode, goBack: () -> Unit, viewModel: NotesViewModel
) {
    var outputNotes by remember { mutableStateOf(notes ?: "") }
    var notesMode by remember { mutableStateOf(initialMode) }
    val paragraphs =
        outputNotes.split("§").toMutableList()
    val didPressBackButton = {
        when (notesMode) {
            NotesMode.BROWSING -> goBack()
            NotesMode.EDITING -> notesMode = NotesMode.BROWSING
        }
    }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = paragraphs::size
    )
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopBar(
                notesMode,
                didPressBackButton,
                didPressEdit = { notesMode = NotesMode.EDITING })
        },
        bottomBar = {
            BottomBar(notesMode, scope, pagerState, {
                scope.launch {
                    outputNotes += "§"
                }
            }) {
                viewModel.update(outputNotes)
                notesMode = NotesMode.BROWSING
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            PagerWithEffect(pagerState) { pageIndex ->
                when (notesMode) {
                    NotesMode.EDITING -> EditableNotes(paragraphs[pageIndex]) {
                        paragraphs[pageIndex] = it
                        outputNotes = paragraphs.joinToString("§")
                    }

                    NotesMode.BROWSING -> {
                        Text(
                            text = paragraphs[pageIndex],
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BottomBar(
    notesMode: NotesMode,
    scope: CoroutineScope,
    pagerState: PagerState,
    didPressNewPage: () -> Unit,
    didPressConfirm: () -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (notesMode == NotesMode.EDITING) {

            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    didPressConfirm()
                }
            ) {
                Text(stringResource(R.string.confirm))
            }

        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage - 1
                            )
                        }
                    },
                    enabled = pagerState.canScrollBackward
                ) {
                    Icon(Icons.Rounded.ArrowBack, "previous page")
                }

                Text((pagerState.currentPage + 1).toString() + " / " + pagerState.pageCount.toString())

                if (pagerState.canScrollForward) {
                    Button({
                        scope.launch {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1
                            )
                        }
                    }) {
                        Icon(Icons.Rounded.ArrowForward, "next page")
                    }
                } else {
                    Button(didPressNewPage) {
                        Icon(Icons.Rounded.Add, "new page")
                    }
                }
            }
        }
    }
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
private fun TopBar(notesMode: NotesMode, didPressBack: () -> Unit, didPressEdit: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = didPressBack) {
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerWithEffect(pagerState: PagerState, content: @Composable (Int) -> Unit) {
    HorizontalPager(
        modifier = Modifier.fillMaxSize(), state = pagerState
    ) { pageIndex ->
        Card(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    pageAnimation(pagerState, pageIndex)
                },
            shape = RectangleShape
        ) {
            content(pageIndex)
        }
    }
}

enum class NotesMode {
    EDITING, BROWSING
}

@Preview
@Composable
fun NotesPreview() {
    val notes =
        """Lorem ipsum dolor sit amet. Et recusandae illo est vero dolore aut iste dignissimos quo tempora totam. Non vitae accusamus rem consequuntur culpa cum Quis ratione? Vel similique accusamus hic magnam impedit sed internos minima est quibusdam quis ut beatae eveniet.

Aut explicabo accusamus et provident voluptas est ipsam recusandae et expedita odit. Vel similique voluptate et corporis facilis et ipsa voluptatem sed saepe internos eum pariatur quia.

Qui ipsam magnam et molestiae recusandae et nihil dolores et laborum dolorem sed galisum necessitatibus sit eveniet tempora? Aut consequatur commodi non rerum esse 33 quos perspiciatis vel impedit tempora ex vitae quia cum explicabo ipsam.
§Lorem ipsum dolor sit amet. Id corporis ipsum est inventore rerum a facere incidunt. Ut labore maiores sed dolor consequatur hic quis voluptatum et fugit nostrum. Ab odit voluptatem non autem nihil sit dolore nemo!

Aut atque odio in omnis minus vel velit quia ut minima provident ex consequatur voluptates. Eos fugiat corrupti qui libero nulla sit inventore sequi ea facere expedita ut aspernatur nulla non enim vero aut sapiente quia? Id expedita modi ut veniam repellendus qui voluptatum tenetur qui laborum voluptate.

Aut inventore modi sit repellat quaerat aut quisquam iste. Rem cumque minus et nesciunt molestias ad ipsam animi aut enim alias aut unde unde. Aut porro totam non commodi asperiores aut numquam quia et tempore numquam! Non recusandae libero sit nesciunt quia quo itaque molestiae ea laudantium modi ut quaerat modi ut omnis doloribus quo harum sunt."""
    Notes(notes, NotesMode.BROWSING, {}, NotesViewModel(BookRepositoryMock()))
}