package com.viseo.mybookshelf.ui.animations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.absoluteValue


@OptIn(ExperimentalFoundationApi::class)
fun GraphicsLayerScope.pageAnimation(
    pagerState: PagerState,
    pageIndex: Int
) {
    // MAKE THE PAGE NOT MOVE
    val pageOffset = pagerState.offsetForPage(pageIndex)
    translationX = size.width * pageOffset

    // ADD THE PAGE CLIPPING
    val endOffset = pagerState.endOffsetForPage(pageIndex)

    shadowElevation = 10f * endOffset.absoluteValue
    shape = PagePath(
        progress = 1f - endOffset.absoluteValue,
        origin = Offset(
            size.width,
            size.height,
        )
    )
    clip = true

    // FADE AWAY
    val startOffset = pagerState.startOffsetForPage(pageIndex)
    alpha = (2f - startOffset) / 2f
}

// ACTUAL OFFSET
@OptIn(ExperimentalFoundationApi::class)
private fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

// OFFSET ONLY FROM THE LEFT
@OptIn(ExperimentalFoundationApi::class)
private fun PagerState.startOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtLeast(0f)
}

// OFFSET ONLY FROM THE RIGHT
@OptIn(ExperimentalFoundationApi::class)
private fun PagerState.endOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtMost(0f)
}

class PagePath(private val progress: Float, private val origin: Offset = Offset(0f, 0f)) : Shape {
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