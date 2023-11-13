package com.example.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey val title: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "notes") val notes: String
)