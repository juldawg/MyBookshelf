package com.example.mybookshelf.ui.theme.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mybookshelf.ui.theme.NotesViewModel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesView(
    viewModel: NotesViewModel = hiltViewModel(),
    bookTitle: String, goBack: () -> Unit,
    initialMode: NotesMode = NotesMode.BROWSING
) {
    val notes by viewModel.getNotes(bookTitle).collectAsState(null)
    if (viewModel.isInitialized) {
        Notes(notes, initialMode, goBack, viewModel)
    } else {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(60.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Notes(
    notes: String?,
    initialMode: NotesMode,
    goBack: () -> Unit,
    viewModel: NotesViewModel
) {
    var outputNotes by remember { mutableStateOf(notes ?: "") }
    var notesMode by remember { mutableStateOf(initialMode) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
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
        Column(
            modifier = Modifier
                .padding(vertical = padding.calculateTopPadding())
                .fillMaxSize()
        ) {
            when (notesMode) {
                NotesMode.EDITING -> EditableNotes(outputNotes) { outputNotes = it }
                NotesMode.BROWSING -> {
                    PagerWithEffect(outputNotes = outputNotes)
                }
            }
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PagerWithEffect(outputNotes: String) {

    val paragraphs = remember {
        outputNotes.split("\n\n") // Split text into paragraphs, adjust as needed
    }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = paragraphs::size
    )
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize(),
            state = pagerState
        ) { pageIndex ->
            var offsetY by remember { mutableFloatStateOf(0f) }
            Card(modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // MAKE THE PAGE NOT MOVE
                    val pageOffset = pagerState.offsetForPage(pageIndex)
                    translationX = size.width * pageOffset

                    // ADD THE CIRCULAR CLIPPING
                    val endOffset = pagerState.endOffsetForPage(pageIndex)

                    shadowElevation = 10f * endOffset.absoluteValue
                    shape = CirclePath(
                        progress = 1f - endOffset.absoluteValue,
                        origin = Offset(
                            size.width,
                            offsetY,
                        )
                    )
                    clip = true

                    // FADE AWAY
                    val startOffset = pagerState.startOffsetForPage(pageIndex)
                    alpha = (2f - startOffset) / 2f
                }
                .pointerInteropFilter {
                    offsetY = it.y
                    false
                }) {
                Text(
                    text = paragraphs[pageIndex],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button({
                scope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage - 1
                    )
                }
            }) {
                Icon(Icons.Rounded.ArrowBack, "previous page")
            }
            Button({
                scope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage + 1
                    )
                }
            }) {
                Icon(Icons.Rounded.ArrowForward, "next page")
            }
        }
    }
}

// ACTUAL OFFSET
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

// OFFSET ONLY FROM THE LEFT
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.startOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtLeast(0f)
}

// OFFSET ONLY FROM THE RIGHT
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.endOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtMost(0f)
}

class CirclePath(private val progress: Float, private val origin: Offset = Offset(0f, 0f)) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val topLeftCorner = Offset(0f, 0f)
        val topRightCorner = Offset(size.width, 0f)
        val bottomRightCorner = Offset(size.width, size.height)
        val bottomLeftCorner = Offset(0f, size.height)
        return Outline.Generic(
            Path().apply {
                moveTo(topLeftCorner.x, topLeftCorner.y)
                lineTo(topRightCorner.x, topRightCorner.y)
                lineTo(bottomRightCorner.x, bottomRightCorner.y)
                lineTo(
                    (origin.x - bottomLeftCorner.x) * (1f - progress),
                    bottomLeftCorner.y
                )
                cubicTo(
                    (origin.x - topLeftCorner.x) * (1f - progress * progress),
                    bottomLeftCorner.y,
                    (origin.x - topLeftCorner.x) * (1f - progress * progress * progress * progress),
                    origin.y,
                    (origin.x - topLeftCorner.x) * (1f - progress * progress * progress * progress),
                    topLeftCorner.y
                )
            }
        )
    }
}