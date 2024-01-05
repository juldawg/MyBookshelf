package com.viseo.mybookshelf.ui.common.extensions

import com.viseo.mybookshelf.domain.Book

fun Book.getAuthors(): String = authors.firstOrNull() ?: "Unknown author"